/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.graphconnector;


import org.apache.tinkerpop.gremlin.driver.*;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.graphconnector.utils.EdgeLabelsLineageGraph;
import org.odpi.openmetadata.graphconnector.utils.VertexLabelsLineageGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

public class JanusGraphRemote extends JanusGraphEmbedded {

    private static final Logger log = LoggerFactory.getLogger(JanusGraphRemote.class);

    protected Client client;


    public JanusGraphRemote(ConnectionProperties connectionProperties){
        super(connectionProperties);
    }


    @Override
    public GraphTraversalSource openGraph(){
        Cluster cluster = cluster();
        createSchema();
        return traversal().withRemote(DriverRemoteConnection.using(cluster,"g"));
    }

    @Override
    public void createSchema(){

        log.info("creating schema");
        //labels
        for (String label : getLabelsFromEnum(VertexLabelsLineageGraph.class)) {
            sendRemoteRequest("VertexLabel",label);
        }
        //edges
        for (String label : getLabelsFromEnum(EdgeLabelsLineageGraph.class)) {
            sendRemoteRequest("EdgeLabel",label);
        }
    }

    public Cluster cluster() {
        Cluster cluster = null;
        try {

            GryoMapper.Builder builder = GryoMapper.build().addRegistry(JanusGraphIoRegistry.getInstance());
            Cluster.Builder clusterBuilder = Cluster.build().addContactPoint("localhost")
//                        .minConnectionPoolSize(4)
//                        .maxConnectionPoolSize(6)
//                        .maxInProcessPerConnection(32)
//                        .maxSimultaneousUsagePerConnection(32)
                    .serializer(new GryoMessageSerializerV1d0(builder)) //TODO: Check this setting. Binary serializer was not working.
                    .port(8182);
            cluster = clusterBuilder.create();
            client = cluster.connect();

        } catch (Exception e) {
            log.error("Error in connecting to host address.", e);
        }
        return cluster;
    }

    private void sendRemoteRequest(String type, String name) {
        final StringBuilder s = new StringBuilder();
        s.append("JanusGraphManagement management = graph.openManagement(); ");
        s.append("if (management.get"+type+"(\""+name+"\") == null) { management.make"+type+"(\""+name+"\").make(); management.commit(); } ");
        log.debug(s.toString());
        // submit the request to the server
        ResultSet resultSet = client.submit(s.toString());
        // drain the results completely
        Stream<Result> futureList = resultSet.stream();
        futureList.map(Result::toString).forEach(log::debug);
    }

    private <T extends Enum<T>> Set<String> getLabelsFromEnum(Class<T> aEnum) {
        return Stream.of(aEnum.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}

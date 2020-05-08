/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.graphconnector;


import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.MessageSerializer;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GraphBinaryMessageSerializerV1;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0;
import org.apache.tinkerpop.gremlin.driver.ser.Serializers;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.io.binary.GraphBinaryIo;
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import static org.apache.tinkerpop.gremlin.process.traversal.TraversalSource.Symbols.withRemote;

public class JanusGraphRemote extends JanusGraphEmbedded {

    private static final Logger log = LoggerFactory.getLogger(JanusGraphRemote.class);

    protected Client client;


    public JanusGraphRemote(ConnectionProperties connectionProperties){
        super(connectionProperties);
    }


    @Override
    public GraphTraversalSource openGraph(){
        return traversal().withRemote(DriverRemoteConnection.using(cluster(),"g"));
    }

    @Override
    public void createSchema(){

        log.info("creating schema");
        // get the schema request as a string
        final String req = schemaRequest();
        // submit the request to the server
        client.submit(req);
    }

    public Cluster cluster() {
        Cluster cluster = null;
        try {

            Cluster.Builder clusterBuilder = Cluster.build().addContactPoint("localhost")
//                        .minConnectionPoolSize(4)
//                        .maxConnectionPoolSize(6)
//                        .maxInProcessPerConnection(32)
//                        .maxSimultaneousUsagePerConnection(32)
                    .serializer(Serializers.GRAPHBINARY_V1D0)
                    .port(8182);
            cluster = clusterBuilder.create();
            client = cluster.connect();

        } catch (Exception e) {
            log.error("Error in connecting to host address.", e);
        }
        return cluster;
    }

    private String schemaRequest(){
        final StringBuilder s = new StringBuilder();

        s.append("JanusGraphManagement management = graph.openManagement(); ");
        s.append("boolean created = false; ");

        // naive check if the schema was previously created
        s.append(
                "if (management.getRelationTypes(RelationType.class).iterator().hasNext()) { management.rollback(); created = false; } else { ");

        // properties

        // vertex labels
        s.append("management.makeVertexLabel(\"titan\").make(); ");

        s.append("management.commit(); created = true; }");

        return s.toString();
    }
}

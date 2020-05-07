/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.graphconnector;


import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.MessageSerializer;
import org.apache.tinkerpop.gremlin.driver.ser.GraphBinaryMessageSerializerV1;
import org.apache.tinkerpop.gremlin.structure.io.binary.GraphBinaryIo;
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;

public class JanusGraphRemote extends JanusGraphEmbedded {

    public JanusGraphRemote(ConnectionProperties connectionProperties){
        super(connectionProperties);
    }


//
//
//
//    public Cluster cluster() {
//        Cluster cluster = null;
//        try {
//
//            GraphBinaryIo.Builder builder = GryoMapper.build().addRegistry(JanusGraphIoRegistry.instance());
//            MessageSerializer serializer = new GraphBinaryMessageSerializerV1(builder);
//
//            Cluster.Builder clusterBuilder = Cluster.build().addContactPoint(hostName)
////                        .minConnectionPoolSize(4)
////                        .maxConnectionPoolSize(6)
////                        .maxInProcessPerConnection(32)
////                        .maxSimultaneousUsagePerConnection(32)
//                    .serializer(serializer)
//                    .port(port);
//            cluster = clusterBuilder.create();
//        } catch (Exception e) {
//            log.error("Error in connecting to host address.", e);
//        }
//        return cluster;
//    }
}

/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.eventprocessors;

import graphconnector.GraphConnector;
import janusconnector.GraphStore;
import org.apache.tinkerpop.gremlin.structure.T;
import org.odpi.openmetadata.accessservices.assetlineage.Edge;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenLineageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class GraphBuilder {

    private static final Logger log = LoggerFactory.getLogger(GraphBuilder.class);

//    private JobConfiguration jobConfiguration;
    private GraphConnector graphConnector;
    private GraphStore test;

    private OpenLineageConfig openLineageConfig;
    public GraphBuilder(OpenLineageConfig openLineageConfig) {
//        this.jobConfiguration = new JobConfiguration();
        graphConnector = new GraphConnector(openLineageConfig);
        Object t = graphConnector.getGraphStore();

        if(t instanceof GraphStore){
             test = (GraphStore) t;
        }
    }

    /**
     * Creates new Entities in bufferGraph related to Lineage
     *
//   * @param entity  Entity event for creation coming from Asset Lineage OMAS
     */
//    public void createEntity(Map.Entry<String, Set<Edge>> entity) {
        public void createEntity() {

        final String methodName = "createEntity";
        test.addEntity();
        get();
    }

    public void get(){
        System.out.println("----------------------------");

        System.out.println(test.count());
    }
}

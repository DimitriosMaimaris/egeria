/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;

import org.odpi.openmetadata.accessservices.assetlineage.Edge;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.GraphStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class GraphStoringServices {

    private static final Logger log = LoggerFactory.getLogger(GraphStoringServices.class);

    private GraphStore graphStore;

    public GraphStoringServices(GraphStore graphStore) {
        this.graphStore = graphStore;
    }

    public void test(){
        graphStore.test();
    }

    public void addEntity(Map<String, Set<Edge>> context){
        graphStore.addEntity(context);
    }

}

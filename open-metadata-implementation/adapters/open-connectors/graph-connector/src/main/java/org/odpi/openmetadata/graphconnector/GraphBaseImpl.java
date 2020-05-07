/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.graphconnector;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory;
import org.janusgraph.core.JanusGraphConfigurationException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class GraphBaseImpl {

    private static final Logger log = LoggerFactory.getLogger(GraphBaseImpl.class);

    protected Graph graph;
    protected GraphTraversalSource g;
    protected boolean supportsTransactions;
    protected Map<String,Object> properties;
    public GraphBaseImpl(){}

    /**
     * Constructs a graph using the given properties.
     * @param connectionProperties the properties coming from the request
     */
    public GraphBaseImpl(ConnectionProperties connectionProperties){
        this.properties = connectionProperties.getConfigurationProperties();
    }


    /**
     * Opens the graph instance. If the graph instance does not exist, a new
     * graph instance will be initialized.
     */
    public GraphTraversalSource openGraph() {
        log.info("Trying to open the graph");
//        conf = new PropertiesConfiguration(propFileName);
        try
        {
            graph = GraphFactory.open(properties);
            g = graph.traversal();
            return g;
        }catch (Exception e){
            log.debug("exception: ",e);
            //TODO add proper exception
            throw new RuntimeException();
        }

    }


//    /**
//     * Closes the graph instance.
//     */
//    public void closeGraph() throws Exception {
//        LOGGER.info("closing graph");
//        try {
//            if (g != null) {
//                g.close();
//            }
//            if (graph != null) {
//                graph.close();
//            }
//        } finally {
//            g = null;
//            graph = null;
//        }
//    }

    /**
     * Drops the graph instance. The default implementation does nothing.
     */
    public void dropGraph() throws Exception {
    }

    /**
     * Creates the graph schema. The default implementation does nothing.
     */
    protected void createSchema() {
    }


    /**
     * Makes an update to the existing graph structure. Does not create any
     * new vertices or edges.
     */
    public void updateElements() {

    }


//    /**
//     * Run the entire application:
//     * 1. Open and initialize the graph
//     * 2. Define the schema
//     * 3. Build the graph
//     * 4. Run traversal queries to get data from the graph
//     * 5. Make updates to the graph
//     * 6. Close the graph
//     */
//    public void runApp() {
//        try {
//            // open and initialize the graph
//            openGraph();
//
//            // define the schema before loading data
//            if (supportsSchema) {
//                createSchema();
//            }
//
//            // build the graph structure
//            createElements();
//            // read to see they were made
//            readElements();
//
//            for (int i = 0; i < 3; i++) {
//                try {
//                    Thread.sleep((long) (Math.random() * 500) + 500);
//                } catch (InterruptedException e) {
//                    LOGGER.error(e.getMessage(), e);
//                }
//                // update some graph elements with changes
//                updateElements();
//                // read to see the changes were made
//                readElements();
//            }
//
//            // delete some graph elements
//            deleteElements();
//            // read to see the changes were made
//            readElements();
//
//            // close the graph
//            closeGraph();
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//        }
//    }

}

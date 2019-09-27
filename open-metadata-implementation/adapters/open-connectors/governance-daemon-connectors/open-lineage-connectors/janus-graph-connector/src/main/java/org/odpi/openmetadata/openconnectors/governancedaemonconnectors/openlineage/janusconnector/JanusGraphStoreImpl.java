package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineage.janusconnector;

import org.janusgraph.core.JanusGraph;

public class JanusGraphStoreImpl implements GraphStore {

    private JanusGraph graph;

    public JanusGraphStoreImpl(JanusGraph graph){
        this.graph = graph;
    }

    @Override
    public void test() {
        System.out.println("fdfdf");
    }
}

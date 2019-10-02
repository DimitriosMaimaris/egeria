package janusconnector;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JanusGraphStoreImpl implements GraphStore {

    private static final Logger log = LoggerFactory.getLogger(JanusGraphStoreImpl.class);

    private JanusGraph graph;

    public JanusGraphStoreImpl(JanusGraph graph){
        this.graph = graph;
    }


    @Override
    public void addEntity() {
        GraphTraversalSource g = graph.traversal();

        try {
        g.addV("test");
        g.addV("test1");

    } catch (Exception e) {
        log.error("{} Creation of control vertex failed, exception {}", "dfdf", e.getMessage());
        g.tx().rollback();
        throw e;
    }
        g.tx().commit();
    }

    @Override
    public void addRelationship() {

    }

    @Override
    public Long count(){
        GraphTraversalSource g = graph.traversal();
        return  g.V().count().next();
    }
}

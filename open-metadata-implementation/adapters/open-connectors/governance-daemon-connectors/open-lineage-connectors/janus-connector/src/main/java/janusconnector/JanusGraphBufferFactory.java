package janusconnector;


import janusconnector.model.OpenLineageErrorCode;
import janusconnector.model.ffdc.OpenLineageException;
import janusconnector.utils.EdgeLabels;
import janusconnector.utils.VertexLabels;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.schema.JanusGraphManagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static janusconnector.utils.GraphConstants.*;


public class JanusGraphBufferFactory extends IndexingFactory{

    private static final Logger log = LoggerFactory.getLogger(JanusGraphBufferFactory.class);

    public JanusGraph openGraph() {

        final String methodName = "open";
        JanusGraph janusGraph;

        JanusGraphFactory.Builder config = JanusGraphFactory.build().
                set("storage.backend", "cql").
                set("storage.hostname", "127.0.0.1").
                set("index.search.backend", "elasticsearch").
                set("index.search.hostname", "127.0.0.1:9200");

//        final String storagePath = "./egeria-lineage-repositories/buffer/berkeley";
//        final String indexPath = "./egeria-lineage-repositories/buffer/searchindex";
//
//        JanusGraphFactory.Builder config = JanusGraphFactory.build().
//                set("storage.backend", "berkeleyje").
//                set("storage.directory", storagePath).
//                set("index.search.backend", "lucene").
//                set("index.search.directory", indexPath);
        try {

            janusGraph = config.open();

        } catch (Exception e) {
            log.error("{} could not open graph with error: {}", "open", e.getMessage());
            OpenLineageErrorCode errorCode = OpenLineageErrorCode.CANNOT_OPEN_GRAPH_DB;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, JanusGraphBufferFactory.class.getName());

            throw new OpenLineageException(400,
                    JanusGraphBufferFactory.class.getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        initializeGraph(janusGraph);
        return janusGraph;
    }

    public void initializeGraph(JanusGraph graph) {

        final String methodName = "initializeGraph";
        /*
         * Create labels for vertex and edge uses
         */
        try {
            log.debug("Initialize graph schema or update it if changes found");
            JanusGraphManagement management = graph.openManagement();

            Set<String> vertexLabels = Stream.of(VertexLabels.values())
                    .map(Enum::name)
                    .collect(Collectors.toSet());


            Set<String> relationshipsLabels = Stream.of(EdgeLabels.values())
                    .map(Enum::name)
                    .collect(Collectors.toSet());

            // Each vertex has a label that reflects the Asset
            management = checkAndAddLabelVertexOrEdge(vertexLabels, management);
            // Each edge has a label that reflects the Relational Type
            management = checkAndAddLabelVertexOrEdge(relationshipsLabels, management);

            management.commit();

            createIndexes(graph);



        } catch (Exception e) {

            OpenLineageErrorCode errorCode = OpenLineageErrorCode.GRAPH_INITIALIZATION_ERROR;
            String errorMessage = errorCode.getErrorMessageId();
            throw new OpenLineageException(400,
                    JanusGraphBufferFactory.class.getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    public void createIndexes(JanusGraph graph){

        createCompositeIndexForVertexProperty(PROPERTY_NAME_GUID,PROPERTY_KEY_ENTITY_GUID,true,graph);
        createCompositeIndexForVertexProperty(PROPERTY_NAME_LABEL,PROPERTY_KEY_LABEL,false,graph);
        createCompositeIndexForVertexProperty(PROPERTY_NAME_DISPLAY_NAME,PROPERTY_KEY_DISPLAY_NAME,false,graph);
        createCompositeIndexForEdgeProperty(PROPERTY_NAME_LABEL,PROPERTY_KEY_RELATIONSHIP_LABEL,graph);

    }    private static JanusGraphManagement checkAndAddLabelVertexOrEdge(Set<String> labels, JanusGraphManagement management){
        for (String label: labels) {

            if (management.getVertexLabel(label) == null)
                management.makeVertexLabel(label).make();
        }

        return management;

    }
}

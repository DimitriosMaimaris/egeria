package graphconnector;

import janusconnector.JanusGraphConnector;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenLineageConfig;

public class GraphFactory<T> implements GraphStore {
    private OpenLineageConfig openLineageConfig;

    public GraphFactory(OpenLineageConfig openLineageConfig){
        this.openLineageConfig = openLineageConfig;
    }

    @Override
    public T getGraph(){

        final String bufferGraph = openLineageConfig.getBufferGraph();

        if(bufferGraph.equals("janus")){
            JanusGraphConnector janusGraphConnector = new JanusGraphConnector();
            return (T) janusGraphConnector.getJanusGraphStore();

        }
        return null;
    }

}

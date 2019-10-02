package graphconnector;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

public class GraphProvider extends ConnectorProviderBase {

    /**
     * The Connector type guid.
     */
    static final String  connectorTypeGUID = "461c4d35-7bfc-4ecb-8478-b671377b7b5a";
    /**
     * The Connector type name.
     */
    static final String  connectorTypeName = "Generic Graph Connector";
    /**
     * The Connector type description.
     */
    static final String  connectorTypeDescription = "Connector supports retrieving and storing data assets from Graph clusters.";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public GraphProvider() {
        Class connectorClass = GraphConnector.class;
        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}

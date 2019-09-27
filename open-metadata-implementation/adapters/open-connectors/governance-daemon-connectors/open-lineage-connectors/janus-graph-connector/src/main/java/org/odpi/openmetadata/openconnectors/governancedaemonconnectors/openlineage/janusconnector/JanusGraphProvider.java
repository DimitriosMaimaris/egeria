package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineage.janusconnector;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

public class JanusGraphProvider extends ConnectorProviderBase {

    static final String connectorTypeGUID = "1c7248e1-476b-4d28-aeb3-441e3bc9888e";
    static final String connectorTypeName = "Janus Graph server Connector";
    static final String connectorTypeDescription = "Connector supports storing of the open metadata cohort registry in a file.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public JanusGraphProvider() {
        Class connectorClass = JanusGraphProvider.class;
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

package graphconnector;

import org.odpi.openmetadata.adminservices.configuration.properties.OpenLineageConfig;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.auditable.AuditableConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The JanusGraphConnector is a connector to a Janus graph implementation that uses a graph store
 * for its persistence.
 */
public class GraphConnector<T> extends ConnectorBase implements AuditableConnector {

    private static final Logger log = LoggerFactory.getLogger(GraphConnector.class);
    private OMRSAuditLog omrsAuditLog;
    private GraphConnectorAuditCode auditLog;
    private OpenLineageConfig openLineageConfig;

    private String serverAddress = null;

    private T graphStore;
        /**
         * Default constructor used by the Open lineage Connector Provider.
         */


        public GraphConnector(OpenLineageConfig openLineageConfig)
        {
            this.graphStore = (T) new GraphFactory(openLineageConfig).getGraph();
        }

    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId  - unique id for the connector instance - useful for messages etc
     * @param connectionProperties - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties) {

        final String actionDescription = "initialize";

        this.connectorInstanceId = connectorInstanceId;
        this.connectionProperties = connectionProperties;

        EndpointProperties endpoint = connectionProperties.getEndpoint();

        super.initialize(connectorInstanceId, connectionProperties);

        if (omrsAuditLog != null) {
            auditLog = GraphConnectorAuditCode.CONNECTOR_INITIALIZING;
            omrsAuditLog.logRecord(actionDescription,
                                   auditLog.getLogMessageId(),
                                   auditLog.getSeverity(),
                                   auditLog.getFormattedLogMessage(),
                                  null,
                                   auditLog.getSystemAction(),
                                   auditLog.getUserAction());
        }

        if (endpoint != null) {
            serverAddress = endpoint.getAddress();

            if (serverAddress != null) {
                log.info("The connecting cassandra cluster server address is: {}.", serverAddress);

            } else {
                log.error("Errors in the Cassandra server configuration. The address of the server cannot be extracted.");
                if (omrsAuditLog != null) {
                    auditLog = GraphConnectorAuditCode.CONNECTOR_SERVER_CONFIGURATION_ERROR;
                    omrsAuditLog.logRecord(actionDescription,
                            auditLog.getLogMessageId(),
                            auditLog.getSeverity(),
                            auditLog.getFormattedLogMessage(),
                            null,
                            auditLog.getSystemAction(),
                            auditLog.getUserAction());
                }
            }
        } else {
            log.error("Errors in Cassandra server address. The endpoint containing the server address is invalid.");
            if (omrsAuditLog != null) {
                auditLog = GraphConnectorAuditCode.CONNECTOR_SERVER_ADDRESS_ERROR;
                omrsAuditLog.logRecord(actionDescription,
                        auditLog.getLogMessageId(),
                        auditLog.getSeverity(),
                        auditLog.getFormattedLogMessage(),
                        null,
                        auditLog.getSystemAction(),
                        auditLog.getUserAction());
            }
        }

//        startCassandraConnection();

//        if (cluster.getClusterName().equals(clusterName) && omrsAuditLog != null)
//        {
//            auditLog = CassandraConnectorAuditCode.CONNECTOR_INITIALIZED;
//            omrsAuditLog.logRecord(actionDescription,
//                    auditLog.getLogMessageId(),
//                    auditLog.getSeverity(),
//                    auditLog.getFormattedLogMessage(),
//                    null,
//                    auditLog.getSystemAction(),
//                    auditLog.getUserAction());
//        }
    }
    public T getGraphStore() {
        return graphStore;
    }

    /**
     * Pass the instance of OMRS Audit Log
     *
     * @param auditLog audit log object
     */
    public void setAuditLog(OMRSAuditLog auditLog) {
        this.omrsAuditLog = auditLog;
    }

}

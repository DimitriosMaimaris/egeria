package janusconnector;

import org.janusgraph.core.JanusGraph;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.auditable.AuditableConnector;

import java.util.Objects;

/**
 * The JanusGraphConnector is a connector to a Janus graph implementation that uses a graph store
 * for its persistence.
 */
public class JanusGraphConnector extends ConnectorBase  implements AuditableConnector {

    private GraphStore janusGraphStore;
    private OMRSAuditLog omrsAuditLog;

    /**
     * Default constructor used by the Open lineage Connector Provider.
     */
    public JanusGraphConnector()
    {
        JanusGraph graph = initialize();
        this.janusGraphStore = new JanusGraphStoreImpl(graph);
    }

    private JanusGraph initialize(){
        JanusGraphBufferFactory janusGraphBuilder = new JanusGraphBufferFactory();
        return janusGraphBuilder.openGraph();
    }

    public GraphStore getJanusGraphStore() {
        return janusGraphStore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JanusGraphConnector that = (JanusGraphConnector) o;
        return Objects.equals(janusGraphStore, that.janusGraphStore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), janusGraphStore);
    }

    @Override
    public void setAuditLog(OMRSAuditLog auditLog) {
        this.omrsAuditLog = auditLog;
    }
}

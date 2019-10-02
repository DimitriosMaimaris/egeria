package janusconnector;

public interface GraphStore {

    void addEntity();
    void addRelationship();
    Long count();
}

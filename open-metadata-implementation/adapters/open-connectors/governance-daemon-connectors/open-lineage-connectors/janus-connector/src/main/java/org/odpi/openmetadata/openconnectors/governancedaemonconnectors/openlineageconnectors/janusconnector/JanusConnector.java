/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.janusgraph.core.JanusGraph;
import org.odpi.openmetadata.accessservices.assetlineage.LineageEntity;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.accessservices.assetlineage.Edge;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.JanusFactory.openBufferGraph;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class JanusConnector extends ConnectorBase implements GraphStore {

    private static final Logger log = LoggerFactory.getLogger(JanusConnector.class);
    private JanusGraph bufferGraph;
    private GraphVertexMapper graphVertexMapper = new GraphVertexMapper();
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

        super.initialize(connectorInstanceId, connectionProperties);
        this.bufferGraph = openBufferGraph(connectionProperties);
    }

    @Override
    public void test() {
        GraphTraversalSource g =  bufferGraph.traversal();
        System.out.println(g.V().count().next());
    }

    @Override
    public void addEntity(Map<String, Set<Edge>> context){
        final String methodName = "createEntity";

        GraphTraversalSource g =  bufferGraph.traversal();

        long begin = System.currentTimeMillis();
        try {
            Set<Edge> test = new HashSet<>();
            context.entrySet().stream().forEach(entry ->
                    {
                        if(entry.getValue().size()>1){
                            test.addAll(entry.getValue());
//                            entry.getValue().stream().forEach(setEntry -> test.);
                        }else {
                            test.add(entry.getValue().stream().findFirst().get());
                        }
                    }
            );
//            context.entrySet().parallelStream().forEach(entry ->
//                    {
//                        if(entry.getValue().size()>1){
//                            entry.getValue().stream().forEach(setEntry -> addVerticesAndRelationship(g,setEntry));
//                        }else {
//                            addVerticesAndRelationship(g,entry.getValue().stream().findFirst().get());
//                        }
//                    }
//                    );
            context.entrySet().forEach(entry -> System.out.println(entry.getValue()));
            System.out.println(test.size());
            test.stream().forEach(entry -> addVerticesAndRelationship(g,entry));
        }catch (Exception e){
            System.out.println("ERRORRRRRRR");
            log.error(e.getMessage());
        }



        long end = System.currentTimeMillis();

        float sec = (end - begin) / 1000F;
        System.out.println(sec + " seconds");    }


    private void addVerticesAndRelationship(GraphTraversalSource g, Edge nodeToNode){
        LineageEntity fromEntity = nodeToNode.getFromVertex();
        LineageEntity toEntity = nodeToNode.getToVertex();

        Vertex vertexFrom = addVertex(g,fromEntity);
        Vertex vertexTo = addVertex(g,toEntity);

        //add check gia null vertex
        addRelationship(nodeToNode.getRelationshipGuid(),nodeToNode.getRelationshipType(),vertexFrom,vertexTo);

    }
    private  Vertex addVertex(GraphTraversalSource g,LineageEntity lineageEntity){
        final String methodName = "addVertex";

        Iterator<Vertex> vertexIt = g.V().has(PROPERTY_KEY_ENTITY_GUID, lineageEntity.getGuid());
        Vertex vertex;


        if(!vertexIt.hasNext()){
            vertex = g.addV(lineageEntity.getTypeDefName()).next();
            addPropertiesToVertex(g,vertex,true,lineageEntity);
            g.tx().commit();
        }
        else {
            vertex = vertexIt.next();
            Object isProxy = g.V().hasLabel(lineageEntity.getTypeDefName()).has(PROPERTY_KEY_ENTITY_GUID, lineageEntity.getGuid())
                    .values(PROPERTY_KEY_PROXY).next();
            if(Boolean.valueOf(isProxy.toString())){
                addPropertiesToVertex(g,vertex,false,lineageEntity);
                g.tx().commit();
            }
            else {
                log.debug("{} found existing vertex {}", methodName, vertex);
                g.tx().rollback();
            }

        }
        return vertex;
    }

    /**
     * Creates new Relationships and it's properties in bufferGraph and mainGraph related to Lineage.
     *
     */
    private void addRelationship(String relationshipGuid,String relationshipType,Vertex fromVertex,Vertex toVertex){
        String methodName = "addRelationship";

       // Begin a graph transaction. Locate the vertices for the ends, and create an edge between them.
        if (relationshipType == null) {
            log.error("{} Relationship type name is missing", methodName);
            throwException(JanusConnectorErrorCode.RELATIONSHIP_TYPE_NAME_NOT_KNOWN,relationshipGuid,methodName);
        }

        GraphTraversalSource g = bufferGraph.traversal();

        Iterator<org.apache.tinkerpop.gremlin.structure.Edge> edgeIt = g.E().has(PROPERTY_KEY_RELATIONSHIP_GUID, relationshipGuid);
            if (edgeIt.hasNext()) {
                org.apache.tinkerpop.gremlin.structure.Edge edge = edgeIt.next();
                log.error("{} found existing edge {}", methodName, edge);
                g.tx().rollback();
                throwException(JanusConnectorErrorCode.RELATIONSHIP_ALREADY_EXISTS,relationshipGuid,methodName);
            }
        org.apache.tinkerpop.gremlin.structure.Edge edge = fromVertex.addEdge(relationshipType, toVertex);

            g.tx().commit();
    }

    private void addPropertiesToVertex(GraphTraversalSource g,Vertex vertex, boolean isProxy, LineageEntity lineageEntity){
        final String methodName = "addPropertiesToVertex";

        try {
            vertex.property(PROPERTY_KEY_PROXY, isProxy);
            graphVertexMapper.mapEntityToVertex(lineageEntity, vertex);
        }catch (Exception e) {
            log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
            g.tx().rollback();
            throwException(JanusConnectorErrorCode.ENTITY_NOT_CREATED,lineageEntity.getGuid(),methodName);

        }
    }

    private void throwException(JanusConnectorErrorCode errorCode,String guid,String methodName){

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid, methodName,
                this.getClass().getName());

        throw new JanusConnectorException(400,
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }
}

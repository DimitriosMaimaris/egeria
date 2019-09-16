/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.listeners;


import org.odpi.openmetadata.accessservices.assetlineage.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.Edge;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ProcessHandler;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.ProcessLineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.*;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageInstanceHandler;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

public class AssetLineageOMRSTopicListener implements OMRSTopicListener {

    private static final Logger log = LoggerFactory.getLogger(AssetLineageOMRSTopicListener.class);
    private static AssetLineageInstanceHandler instanceHandler = new AssetLineageInstanceHandler();
    private List<String> supportedZones;
    private AssetLineagePublisher publisher;
    private String serverName;
    private String serverUserName;
    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetLineageOutTopic connection to the out topic
     * @param supportedZones       list of zones covered by this instance of the access service.
     * @param auditLog             log for errors and information messages
     * @param serverUserName       name of the user of the server instance
     * @param serverName           name of this server instance
     */
    public AssetLineageOMRSTopicListener(Connection assetLineageOutTopic,
                                         List<String> supportedZones,
                                         OMRSAuditLog auditLog,
                                         String serverUserName,
                                         String serverName) throws OMAGConfigurationErrorException {

        this.supportedZones = supportedZones;
        this.serverName = serverName;
        this.serverUserName = serverUserName;
        publisher = new AssetLineagePublisher(assetLineageOutTopic, auditLog);
    }

    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    public void processRegistryEvent(OMRSRegistryEvent event) {
        log.debug("Ignoring registry event: {} " ,event);
    }


    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    public void processTypeDefEvent(OMRSTypeDefEvent event) {
        log.debug("Ignoring type event: {} ", event);
    }


    /**
     * Unpack and deliver an instance event to the InstanceEventProcessor
     *
     * @param instanceEvent event to unpack
     */
    public void processInstanceEvent(OMRSInstanceEvent instanceEvent) {

        final String serviceOperationName = "processInstanceEvent";

        log.debug("Processing instance event {}",instanceEvent);

        if (instanceEvent == null) {
            log.debug("Null instance event - Asset LIneage OMAS is ignoring the event");
        } else {
            OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
            OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();

            if (instanceEventOriginator != null) {
                switch (instanceEventType) {
                    //TODO change Process to UPDATE event when Data engine changed is finished
                    case NEW_ENTITY_EVENT:
                        processNewEntityEvent(instanceEvent.getEntity(), serviceOperationName);
                        break;
                    case NEW_RELATIONSHIP_EVENT:
                        processRelationship(instanceEvent.getRelationship(), serviceOperationName);
                        break;
                    default:
                        break;
                }
            }

        }
    }

    /**
     * Takes the entity from NEW ENTITY EVENT and process the event if the type is
     * of specific Types that Asset Lineage OMAS is interested in processing
     *
     * @param entityDetail         event to validate
     * @param serviceOperationName typically the top-level methodName that makes the call
     */
    public void processNewEntityEvent(EntityDetail entityDetail, String serviceOperationName) {
        final String typeDefName = entityDetail.getType().getTypeDefName();

        if (!isValidEntityEvent(typeDefName)) {
            log.info(("Event is ignored as the entity is not relevant type for the Asset Lineage OMAS."));
        } else {
            processNewEntity(entityDetail, typeDefName, serviceOperationName);
        }
    }

    /**
     * Takes the entity  based on the type of the entity it delegates to the appropriate method
     *
     * @param entityDetail         entity to get context
     * @param typeName             the type of the entity
     * @param serviceOperationName typically the top-level methodName that makes the call
     */
    private void processNewEntity(EntityDetail entityDetail, String typeName, String serviceOperationName) {

        try {
            if (typeName.equals(PROCESS)) {
                getContextForProcess(entityDetail, serviceOperationName);
            }
            else {
                getAssetContext(entityDetail, serviceOperationName);
            }
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
                throw new AssetLineageException(e.getReportedHTTPCode(),
                                                e.getReportingClassName(),
                                                e.getReportingActionDescription(),
                                                e.getErrorMessage(),
                                                e.getReportedSystemAction(),
                                                e.getReportedUserAction());
            }


    }

    /**
     * Takes the context for a Process and publishes the event to the Cohort
     *
     * @param entityDetail         entity to get context
     * @param serviceOperationName typically the top-level methodName that makes the call
     */
    private void getContextForProcess(EntityDetail entityDetail, String serviceOperationName) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException {

        ProcessHandler processHandler = instanceHandler.getProcessHandler(serverUserName, serverName, serviceOperationName);
        Map<String, Set<Edge>>  processContext = processHandler.getProcessContext(serverUserName, entityDetail.getGUID());

        ProcessLineageEvent event = new ProcessLineageEvent();
        event.setProcessContext(processContext);

        publisher.publishRelationshipEvent(event);
    }

    private void getAssetContext(EntityDetail entityDetail, String serviceOperationName) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException {
        String technicalGuid = entityDetail.getGUID();

        ContextHandler newContextHandler = instanceHandler.getContextHandler(serverUserName,serverName,serviceOperationName);
        AssetContext assetContext = newContextHandler.getAssetContext(serverUserName,technicalGuid);

        GlossaryHandler glossaryHandler = instanceHandler.getGlossaryHandler(serverUserName,serverName,serviceOperationName);
        Map<String,Set<Edge>> context =  glossaryHandler.getGlossaryTerm(technicalGuid,serverUserName,entityDetail,assetContext);

        ProcessLineageEvent event = new ProcessLineageEvent();
        if(context.size() != 0){
            event.setProcessContext(context);
        }
        else
        {
            event.setProcessContext(assetContext.getNeighbors());
        }


        publisher.publishRelationshipEvent(event);
    }

    /**
     * @param relationship         details of the new relationship
     * @param serviceOperationName typically the top-level methodName that makes the call
     */
    public void processRelationship(Relationship relationship,String serviceOperationName) {

        if (!isValidRelationshipEvent(relationship))
            log.info("Event is ignored as the relationship is not a semantic assignment for a column or table");
        else {
            log.info("Processing semantic assignment relationship event");

            try {
                processSemanticAssignment(relationship, serviceOperationName);
            } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException  e) {
                throw new AssetLineageException(e.getReportedHTTPCode(),
                                                e.getReportingClassName(),
                                                e.getReportingActionDescription(),
                                                e.getErrorMessage(),
                                                e.getReportedSystemAction(),
                                                e.getReportedUserAction());
            }
        }

    }

    private boolean isValidEntityEvent(String typeDefName) {
        final List<String> types = Arrays.asList(PROCESS, GLOSSARY_TERM,TABULAR_SCHEMA_TYPE, TABULAR_COLUMN, RELATIONAL_COLUMN, RELATIONAL_TABLE, DATA_FILE);
        return types.contains(typeDefName);
    }


    /**
     * Only semantic assignments should be handled, and only when columns or tables are involved.
     *
     * @param relationship - details of the new relationship
     * @return
     */
    private boolean isValidRelationshipEvent(Relationship relationship) {
        String entityProxyOneType = relationship.getEntityOneProxy().getType().getTypeDefName();

        return (relationship.getType().getTypeDefName().equals(SEMANTIC_ASSIGNMENT) &&
                (entityProxyOneType.equals(RELATIONAL_COLUMN) || entityProxyOneType.equals(RELATIONAL_TABLE) || entityProxyOneType.equals(TABULAR_COLUMN))
        );
    }


    /**
     * Takes the entities of the the relationship and builds the context for the glossaryTerm
     * and publishes an event.
     * @param relationship - details of the new relationship
     *
     */
    private void processSemanticAssignment(Relationship relationship,String serviceOperationName) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException {

        RelationshipEvent relationshipEvent = new RelationshipEvent();

        GlossaryHandler glossaryHandler = instanceHandler.getGlossaryHandler(serverUserName,serverName,serviceOperationName);
        Map<String,Set<Edge>> glossaryContext = glossaryHandler.getGlossaryFromProxy(relationship,serverUserName);

        relationshipEvent.setTypeDefName(relationship.getType().getTypeDefName());
        relationshipEvent.setRelationshipGuid(relationship.getGUID());
        relationshipEvent.setAssetContext(glossaryContext);
        relationshipEvent.setOmrsInstanceEventType(OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT);

        publisher.publishRelationshipEvent(relationshipEvent);
    }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetlineage.Edge;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.AssetLineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.AssetLineageEntityEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.ProcessLineageEvent;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.services.GraphStoringServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class InTopicListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(InTopicListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OMRSAuditLog auditLog;
    private GraphStoringServices graphStoringServices;

    public InTopicListener(GraphStoringServices graphStoringServices, OMRSAuditLog auditLog) {

        this.graphStoringServices = graphStoringServices;
        this.auditLog = auditLog;

    }


    /**
     * @param eventAsString contains all the information needed to build asset lineage like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String eventAsString) {
        ProcessLineageEvent event = null;
        graphStoringServices.test();
        try {
            event = OBJECT_MAPPER.readValue(eventAsString, ProcessLineageEvent.class);
            log.info("Started processing OpenLineageEvent");
        } catch (Exception e) {
            log.error("Exception processing event from in topic", e);
            OpenLineageErrorCode auditCode = OpenLineageErrorCode.PROCESS_EVENT_EXCEPTION;

            auditLog.logException("processEvent",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(eventAsString, e.getMessage()),
                    e.getMessage(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
        }

//        try {
//            switch (event.getOmrsInstanceEventType()) {
//                case NEW_ENTITY_EVENT:

        ProcessLineageEvent newEntityEvent = null;
        try {
//            if(event.getAssetLineageEntityEvent().equals(AssetLineageEventType.NEW_PROCESS)){
                newEntityEvent = OBJECT_MAPPER.readValue(eventAsString, ProcessLineageEvent.class);

                    graphStoringServices.addEntity(newEntityEvent.getContext());


//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//                    break;
//                case NEW_RELATIONSHIP_EVENT:
//                      NEW_RELATIONSHIP_EVENTT  RelationshipEvent relationshipEvent =OBJECT_MAPPER.readValue(eventAsString, RelationshipEvent.class);
//                    break;
//                case DELETE_PURGED_RELATIONSHIP_EVENT:
//                         DeletePurgedRelationshipEvent deletePurgedRelationshipEvent =OBJECT_MAPPER.readValue(eventAsString, DeletePurgedRelationshipEvent.class);
//                    break;
//            }
//        }catch (IOException e){
//            log.debug(e.getMessage());
//        }

    }

}


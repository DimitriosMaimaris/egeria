/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.accessservices.assetlineage.Edge;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.AssetLineageEvent;

import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class")
public class ProcessLineageEvent {

   private Map<String, Set<Edge>> context;
   private AssetLineageEventType assetLineageEntityEvent;

    public Map<String, Set<Edge>> getContext() {
        return context;
    }

    public void setContext(Map<String, Set<Edge>> processContext) {

        this.context = processContext;
    }

    public AssetLineageEventType getAssetLineageEntityEvent() {
        return assetLineageEntityEvent;
    }

    public void setAssetLineageEntityEvent(AssetLineageEventType assetLineageEntityEvent) {
        this.assetLineageEntityEvent = assetLineageEntityEvent;
    }

}


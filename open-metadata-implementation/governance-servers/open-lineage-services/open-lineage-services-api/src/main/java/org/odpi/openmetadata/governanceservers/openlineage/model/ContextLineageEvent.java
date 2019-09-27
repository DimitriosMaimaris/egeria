/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.model;

import java.util.Map;
import java.util.Set;


public class ContextLineageEvent  {

   private Map<String, Set<Edge>> processContext;

    public Map<String, Set<Edge>> getProcessContext() {
        return processContext;
    }

    public void setProcessContext(Map<String, Set<Edge>> processContext) {

        this.processContext = processContext;
    }


}


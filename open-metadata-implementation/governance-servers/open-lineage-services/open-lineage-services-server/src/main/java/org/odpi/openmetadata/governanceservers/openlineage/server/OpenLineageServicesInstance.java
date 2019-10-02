/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.services.GraphServices;

public class OpenLineageServicesInstance {
    private String serverName;
    private GraphServices graphServices;

    public OpenLineageServicesInstance(
                                       GraphServices graphServices,
                                       String serverName) {
        this.serverName = serverName;
        this.graphServices = graphServices;

        OpenLineageServicesInstanceMap.setNewInstanceForJVM(serverName, this);
    }

    public GraphServices getGraphServices() {
        return this.graphServices;
    }


    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        OpenLineageServicesInstanceMap.removeInstanceForJVM(serverName);
    }

}

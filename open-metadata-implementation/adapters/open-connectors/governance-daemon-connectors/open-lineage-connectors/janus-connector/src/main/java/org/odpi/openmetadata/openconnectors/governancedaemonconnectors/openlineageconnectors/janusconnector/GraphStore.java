/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector;



import org.odpi.openmetadata.accessservices.assetlineage.Edge;

import java.util.Map;
import java.util.Set;

public interface GraphStore {

    void test();
    void addEntity(Map<String, Set<Edge>> context);
}

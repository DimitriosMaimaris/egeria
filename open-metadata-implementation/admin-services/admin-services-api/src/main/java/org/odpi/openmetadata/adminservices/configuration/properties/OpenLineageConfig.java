/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * OpenLineageConfig provides the properties for the open-lineage-services.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenLineageConfig extends AdminServicesConfigHeader {

    private int openLineageId = 0;
    private String openLineageName;
    private String openLineageDescription;
    private String lineageServerURL;
    private String inTopicName;
    private Connection inTopicConnection;

    private String bufferGraph;
    private String mainGraph;
    private String storageBackend;
    private List<String> storageHostanmes;
    private String indexBackend;
    private String indexHostanme;
    private String usernameBackend;
    private char[] passwordBackend;
    private String usernameIndex;
    private char[] passwordIndex;

    /**
     * Default constructor
     */
    public OpenLineageConfig() {
        super();
    }


    /**
     * Set up the default values for open lineage
     *
     * @param template fixed properties about open lineage
     */
    public OpenLineageConfig(OpenLineageConfig template) {
        super(template);

        if (template != null) {
            openLineageId = template.openLineageId;
            openLineageName = template.openLineageName;
            openLineageDescription = template.openLineageDescription;
            lineageServerURL = template.lineageServerURL;
            inTopicName = template.inTopicName;
            inTopicConnection = template.inTopicConnection;
        }
    }

    /**
     * Return the code number (ordinal) for this Open Lineage
     *
     * @return the code numner for Open Lineage component
     */
    public int getOpenLineageId() {
        return openLineageId;
    }

    /**
     * Set up the code number (ordinal) for the Open Lineage
     *
     * @param openLineageId int ordinal
     */
    public void setOpenLineageId(int openLineageId) {
        this.openLineageId = openLineageId;
    }

    /**
     *  Return the name of the Open Lineage Connector
     *
     * @return the name of the open lineage connector
     */
    public String getOpenLineageName() {
        return openLineageName;
    }

    /**
     * Set up the name of the Open Lineage Connector
     *
     * @param openLineageName connector name
     */
    public void setOpenLineageName(String openLineageName) {
        this.openLineageName = openLineageName;
    }

    /**
     * Return the short description of the Open Lineage Component.  The default value is in English but this can be changed.
     *
     * @return String description
     */
    public String getOpenLineageDescription() {
        return openLineageDescription;
    }

    /**
     * Set up the short description of the Open Lineage.
     *
     * @param openLineageDescription String description
     */
    public void setOpenLineageDescription(String openLineageDescription) {
        this.openLineageDescription = openLineageDescription;
    }


    /**
     * Return the URL for the Lineage Server used in the Governance Server Connector
     *
     * @return String URL
     */
    public String getLineageServerURL() {
        return lineageServerURL;
    }

    /**
     * Set up the URL for the Lineage Server used in the Governance Server Connector.
     *
     * @param lineageServerURL String for Governance Server URL
     */
    public void setLineageServerURL(String lineageServerURL) {
        this.lineageServerURL = lineageServerURL;
    }


    /**
     * Return the Input Topic Name for Open Lineage
     *
     * @return String Input Topic name
     */
    public String getInTopicName() {
        return inTopicName;
    }

    /**
     * Set up the Open Lineage In Topic Name
     *
     * @param inTopicName String Open Lineage Name
     */
    public void setInTopicName(String inTopicName) {
        this.inTopicName = inTopicName;
    }

    /**
     * Return the OCF Connection for the In Topic used to pass requests to this Open Lineage.
     * For example, the output topic of Governance Engine OMAS can be provided
     * (e.g. "open-metadata.access-services.GovernanceEngine.outTopic")
     *
     * @return  Connection for In Topic
     */
    public Connection getInTopicConnection() {
        return inTopicConnection;
    }

    /**
     * Set up the OCF Connection for the Out Topic used to pass requests to this Open Lineage.
     *
     * @param inTopicConnection  Connection for In Topic
     */
    public void setInTopicConnection(Connection inTopicConnection) {
        this.inTopicConnection = inTopicConnection;
    }


    public String getBufferGraph() {
        return bufferGraph;
    }

    public void setBufferGraph(String bufferGraph) {
        this.bufferGraph = bufferGraph;
    }

    public String getMainGraph() {
        return mainGraph;
    }

    public void setMainGraph(String mainGraph) {
        this.mainGraph = mainGraph;
    }

    public String getStorageBackend() {
        return storageBackend;
    }

    public void setStorageBackend(String storageBackend) {
        this.storageBackend = storageBackend;
    }

    public List<String> getStorageHostanmes() {
        return storageHostanmes;
    }

    public void setStorageHostanmes(List<String> storageHostanmes) {
        this.storageHostanmes = storageHostanmes;
    }

    public String getIndexBackend() {
        return indexBackend;
    }

    public void setIndexBackend(String indexBackend) {
        this.indexBackend = indexBackend;
    }

    public String getIndexHostanme() {
        return indexHostanme;
    }

    public void setIndexHostanme(String indexHostanme) {
        this.indexHostanme = indexHostanme;
    }

    public String getUsernameBackend() {
        return usernameBackend;
    }

    public void setUsernameBackend(String usernameBackend) {
        this.usernameBackend = usernameBackend;
    }

    public char[] getPasswordBackend() {
        return passwordBackend;
    }

    public void setPasswordBackend(char[] passwordBackend) {
        this.passwordBackend = passwordBackend;
    }

    public String getUsernameIndex() {
        return usernameIndex;
    }

    public void setUsernameIndex(String usernameIndex) {
        this.usernameIndex = usernameIndex;
    }

    public char[] getPasswordIndex() {
        return passwordIndex;
    }

    public void setPasswordIndex(char[] passwordIndex) {
        this.passwordIndex = passwordIndex;
    }

    @Override
    public String toString() {
        return "OpenLineageConfig{" +
                "openLineageId=" + openLineageId +
                ", openLineageName='" + openLineageName + '\'' +
                ", openLineageDescription='" + openLineageDescription + '\'' +
                ", lineageServerURL='" + lineageServerURL + '\'' +
                ", inTopicName='" + inTopicName + '\'' +
                ", inTopicConnection=" + inTopicConnection +
                ", bufferGraph='" + bufferGraph + '\'' +
                ", mainGraph='" + mainGraph + '\'' +
                ", storageBackend='" + storageBackend + '\'' +
                ", storageHostanmes=" + storageHostanmes +
                ", indexBackend='" + indexBackend + '\'' +
                ", indexHostanme='" + indexHostanme + '\'' +
                ", usernameBackend='" + usernameBackend + '\'' +
                ", passwordBackend=" + Arrays.toString(passwordBackend) +
                ", usernameIndex='" + usernameIndex + '\'' +
                ", passwordIndex=" + Arrays.toString(passwordIndex) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenLineageConfig that = (OpenLineageConfig) o;
        return openLineageId == that.openLineageId &&
                Objects.equals(openLineageName, that.openLineageName) &&
                Objects.equals(openLineageDescription, that.openLineageDescription) &&
                Objects.equals(lineageServerURL, that.lineageServerURL) &&
                Objects.equals(inTopicName, that.inTopicName) &&
                Objects.equals(inTopicConnection, that.inTopicConnection) &&
                Objects.equals(bufferGraph, that.bufferGraph) &&
                Objects.equals(mainGraph, that.mainGraph) &&
                Objects.equals(storageBackend, that.storageBackend) &&
                Objects.equals(storageHostanmes, that.storageHostanmes) &&
                Objects.equals(indexBackend, that.indexBackend) &&
                Objects.equals(indexHostanme, that.indexHostanme) &&
                Objects.equals(usernameBackend, that.usernameBackend) &&
                Arrays.equals(passwordBackend, that.passwordBackend) &&
                Objects.equals(usernameIndex, that.usernameIndex) &&
                Arrays.equals(passwordIndex, that.passwordIndex);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(openLineageId, openLineageName, openLineageDescription, lineageServerURL, inTopicName, inTopicConnection, bufferGraph, mainGraph, storageBackend, storageHostanmes, indexBackend, indexHostanme, usernameBackend, usernameIndex);
        result = 31 * result + Arrays.hashCode(passwordBackend);
        result = 31 * result + Arrays.hashCode(passwordIndex);
        return result;
    }

    //    /**
//     * Validate that an object is equal depending on their stored values.
//     *
//     * @param objectToCompare object
//     * @return boolean result
//     */
//    @Override
//    public boolean equals(Object objectToCompare) {
//        if (this == objectToCompare) {
//            return true;
//        }
//        if (objectToCompare == null || getClass() != objectToCompare.getClass()) {
//            return false;
//        }
//        OpenLineageConfig that = (OpenLineageConfig) objectToCompare;
//        return getOpenLineageId() == that.getOpenLineageId() &&
//                Objects.equals(getOpenLineageName(), that.getOpenLineageName()) &&
//                Objects.equals(getOpenLineageDescription(), that.getOpenLineageDescription()) &&
//                Objects.equals(getLineageServerURL(), that.getLineageServerURL()) &&
//                Objects.equals(getInTopicConnection(), that.getInTopicConnection()) &&
//                Objects.equals(getInTopicName(), that.getInTopicName());
//    }
//
//
//
//    /**
//     * Return a hash code based on the values of this object.
//     *
//     * @return in hash code
//     */
//    @Override
//    public int hashCode() {
//        return Objects.hash(getOpenLineageId(), getOpenLineageName(),
//                getOpenLineageDescription(),  getLineageServerURL(), getInTopicConnection(),
//                getInTopicName());
//    }
}
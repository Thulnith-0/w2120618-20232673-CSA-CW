/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.model;

import java.util.Map;

/**
 *
 * @author Thulnith
 */
public class DiscoveryResponse {

    private String apiName;
    private String version;
    private String contact;
    private Map<String, String> resources;

    public DiscoveryResponse() {
    }

    public DiscoveryResponse(String apiName, String version, String contact, Map<String, String> resources) {
        this.apiName = apiName;
        this.version = version;
        this.contact = contact;
        this.resources = resources;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Map<String, String> getResources() {
        return resources;
    }

    public void setResources(Map<String, String> resources) {
        this.resources = resources;
    }
}
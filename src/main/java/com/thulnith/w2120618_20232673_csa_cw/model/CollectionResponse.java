/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.model;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Thulnith
 */
public class CollectionResponse<T> {

    private int status;
    private String message;
    private int count;
    private List<T> data;
    private Map<String, String> links;

    public CollectionResponse() {
    }

    public CollectionResponse(int status, String message, List<T> data, Map<String, String> links) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.count = data != null ? data.size() : 0;
        this.links = links;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
        this.count = data != null ? data.size() : 0;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
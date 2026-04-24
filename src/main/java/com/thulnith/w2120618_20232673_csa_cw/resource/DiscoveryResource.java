/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.resource;

import com.thulnith.w2120618_20232673_csa_cw.model.DiscoveryResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Thulnith
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public DiscoveryResponse getDiscovery() {
        Map<String, String> resources = new LinkedHashMap<>();

        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        resources.put("sensorReadings", "/api/v1/sensors/{sensorId}/readings");

        return new DiscoveryResponse(
                "Smart Campus Sensor & Room Management API",
                "v1",
                "module-support@westminster.ac.uk",
                resources
        );
    }
}
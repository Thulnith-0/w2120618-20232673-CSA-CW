/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.resource;

import com.thulnith.w2120618_20232673_csa_cw.exception.SensorUnavailableException;
import com.thulnith.w2120618_20232673_csa_cw.model.ApiError;
import com.thulnith.w2120618_20232673_csa_cw.model.ApiResponse;
import com.thulnith.w2120618_20232673_csa_cw.model.CollectionResponse;
import com.thulnith.w2120618_20232673_csa_cw.model.Sensor;
import com.thulnith.w2120618_20232673_csa_cw.model.SensorReading;
import com.thulnith.w2120618_20232673_csa_cw.store.DataStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Thulnith
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        validateSensorExists();

        List<SensorReading> sensorReadings = DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
        sensorReadings.sort(Comparator.comparing(SensorReading::getTimestamp));

        Map<String, String> links = new LinkedHashMap<>();
        links.put("self", "/api/v1/sensors/" + sensorId + "/readings");
        links.put("sensor", "/api/v1/sensors/" + sensorId);

        CollectionResponse<SensorReading> response = new CollectionResponse<>(
                200,
                "Sensor readings retrieved successfully.",
                sensorReadings,
                links
        );

        return Response.ok(response).build();
    }

    @GET
    @Path("/{readingId}")
    public Response getReadingById(@PathParam("readingId") String readingId) {
        validateSensorExists();

        List<SensorReading> sensorReadings = DataStore.readings.getOrDefault(sensorId, new ArrayList<>());

        for (SensorReading reading : sensorReadings) {
            if (reading.getId() != null && reading.getId().equals(readingId)) {
                ApiResponse<SensorReading> response = new ApiResponse<>(
                        200,
                        "Sensor reading retrieved successfully.",
                        reading
                );

                return Response.ok(response).build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ApiError(404, "Sensor reading not found."))
                .build();
    }

    @POST
    public Response addReading(SensorReading reading, @Context UriInfo uriInfo) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            throw new NotFoundException("Sensor not found.");
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                    "Cannot add reading. Sensor is currently in MAINTENANCE mode."
            );
        }

        if ("OFFLINE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                    "Cannot add reading. Sensor is currently OFFLINE."
            );
        }

        if (reading == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiError(400, "Reading body is required."))
                    .build();
        }

        if (Double.isNaN(reading.getValue()) || Double.isInfinite(reading.getValue())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiError(400, "Reading value must be a valid number."))
                    .build();
        }

        if (reading.getId() == null || reading.getId().trim().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }

        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }
        
        reading.setSensorId(sensorId);

        DataStore.readings.putIfAbsent(sensorId, new ArrayList<>());
        DataStore.readings.get(sensorId).add(reading);

        sensor.setCurrentValue(reading.getValue());

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(reading.getId())
                .build();

        Map<String, String> links = new LinkedHashMap<>();
        links.put("self", "/api/v1/sensors/" + sensorId + "/readings/" + reading.getId());
        links.put("sensor", "/api/v1/sensors/" + sensorId);
        links.put("readings", "/api/v1/sensors/" + sensorId + "/readings");

        ApiResponse<SensorReading> response = new ApiResponse<>(
                201,
                "Sensor reading added successfully.",
                reading,
                links
        );

        return Response.created(location)
                .entity(response)
                .build();
    }

    private void validateSensorExists() {
        if (!DataStore.sensors.containsKey(sensorId)) {
            throw new NotFoundException("Sensor not found.");
        }
    }
}
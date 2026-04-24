/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.resource;

import com.thulnith.w2120618_20232673_csa_cw.exception.DuplicateResourceException;
import com.thulnith.w2120618_20232673_csa_cw.exception.LinkedResourceNotFoundException;
import com.thulnith.w2120618_20232673_csa_cw.model.ApiError;
import com.thulnith.w2120618_20232673_csa_cw.model.ApiResponse;
import com.thulnith.w2120618_20232673_csa_cw.model.CollectionResponse;
import com.thulnith.w2120618_20232673_csa_cw.model.Room;
import com.thulnith.w2120618_20232673_csa_cw.model.Sensor;
import com.thulnith.w2120618_20232673_csa_cw.store.DataStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Thulnith
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public CollectionResponse<Sensor> getAllSensors(@QueryParam("type") String type,
                                                    @QueryParam("status") String status) {

        List<Sensor> sensors = new ArrayList<>(DataStore.sensors.values());

        if (type != null && !type.trim().isEmpty()) {
            sensors = sensors.stream()
                    .filter(sensor -> sensor.getType() != null
                            && sensor.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }

        if (status != null && !status.trim().isEmpty()) {
            sensors = sensors.stream()
                    .filter(sensor -> sensor.getStatus() != null
                            && sensor.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        sensors.sort(Comparator.comparing(Sensor::getId));

        Map<String, String> links = new LinkedHashMap<>();
        links.put("self", "/api/v1/sensors");

        return new CollectionResponse<>(
                200,
                "Sensors retrieved successfully.",
                sensors,
                links
        );
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiError(404, "Sensor not found."))
                    .build();
        }

        ApiResponse<Sensor> response = new ApiResponse<>(
                200,
                "Sensor retrieved successfully.",
                sensor,
                buildSensorLinks(sensor)
        );

        return Response.ok(response).build();
    }

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {
        if (sensor == null || sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiError(400, "Sensor ID is required."))
                    .build();
        }

        if (sensor.getType() == null || sensor.getType().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiError(400, "Sensor type is required."))
                    .build();
        }

        if (sensor.getStatus() == null || sensor.getStatus().trim().isEmpty()) {
            sensor.setStatus("ACTIVE");
        } else {
            sensor.setStatus(sensor.getStatus().trim().toUpperCase());
        }

        if (!isValidSensorStatus(sensor.getStatus())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiError(400, "Sensor status must be ACTIVE, MAINTENANCE, or OFFLINE."))
                    .build();
        }

        if (DataStore.sensors.containsKey(sensor.getId())) {
            throw new DuplicateResourceException("A sensor with this ID already exists.");
        }

        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiError(400, "roomId is required."))
                    .build();
        }

        Room room = DataStore.rooms.get(sensor.getRoomId());

        if (room == null) {
            throw new LinkedResourceNotFoundException(
                    "Cannot create sensor. The linked room does not exist."
            );
        }

        DataStore.sensors.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());
        DataStore.readings.put(sensor.getId(), new ArrayList<>());

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(sensor.getId())
                .build();

        ApiResponse<Sensor> response = new ApiResponse<>(
                201,
                "Sensor created successfully.",
                sensor,
                buildSensorLinks(sensor)
        );

        return Response.created(location)
                .entity(response)
                .build();
    }

    @PUT
    @Path("/{sensorId}")
    public Response updateSensor(@PathParam("sensorId") String sensorId, Sensor updatedSensor) {
        Sensor existingSensor = DataStore.sensors.get(sensorId);

        if (existingSensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiError(404, "Sensor not found."))
                    .build();
        }

        if (updatedSensor == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiError(400, "Sensor body is required."))
                    .build();
        }

        if (updatedSensor.getType() != null && !updatedSensor.getType().trim().isEmpty()) {
            existingSensor.setType(updatedSensor.getType());
        }

        if (updatedSensor.getStatus() != null && !updatedSensor.getStatus().trim().isEmpty()) {
            String newStatus = updatedSensor.getStatus().trim().toUpperCase();

            if (!isValidSensorStatus(newStatus)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ApiError(400, "Sensor status must be ACTIVE, MAINTENANCE, or OFFLINE."))
                        .build();
            }

            existingSensor.setStatus(newStatus);
        }

        existingSensor.setCurrentValue(updatedSensor.getCurrentValue());

        ApiResponse<Sensor> response = new ApiResponse<>(
                200,
                "Sensor updated successfully.",
                existingSensor,
                buildSensorLinks(existingSensor)
        );

        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiError(404, "Sensor not found."))
                    .build();
        }

        Room room = DataStore.rooms.get(sensor.getRoomId());

        if (room != null && room.getSensorIds() != null) {
            room.getSensorIds().remove(sensorId);
        }

        DataStore.sensors.remove(sensorId);
        DataStore.readings.remove(sensorId);

        Map<String, String> links = new LinkedHashMap<>();
        links.put("sensors", "/api/v1/sensors");
        links.put("room", "/api/v1/rooms/" + sensor.getRoomId());

        ApiResponse<String> response = new ApiResponse<>(
                200,
                "Sensor deleted successfully.",
                sensorId,
                links
        );

        return Response.ok(response).build();
    }

    // Sub-resource locator for sensor readings
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }

    private boolean isValidSensorStatus(String status) {
        return "ACTIVE".equalsIgnoreCase(status)
                || "MAINTENANCE".equalsIgnoreCase(status)
                || "OFFLINE".equalsIgnoreCase(status);
    }

    private Map<String, String> buildSensorLinks(Sensor sensor) {
        Map<String, String> links = new LinkedHashMap<>();

        links.put("self", "/api/v1/sensors/" + sensor.getId());
        links.put("readings", "/api/v1/sensors/" + sensor.getId() + "/readings");
        links.put("room", "/api/v1/rooms/" + sensor.getRoomId());

        return links;
    }
}
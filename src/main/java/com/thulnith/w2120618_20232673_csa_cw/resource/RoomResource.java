/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.resource;

import com.thulnith.w2120618_20232673_csa_cw.exception.DuplicateResourceException;
import com.thulnith.w2120618_20232673_csa_cw.exception.RoomNotEmptyException;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public CollectionResponse<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>(DataStore.rooms.values());
        rooms.sort(Comparator.comparing(Room::getId));

        Map<String, String> links = new LinkedHashMap<>();
        links.put("self", "/api/v1/rooms");

        return new CollectionResponse<>(
                200,
                "Rooms retrieved successfully.",
                rooms,
                links
        );
    }

    @POST
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        if (room == null || room.getId() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiError(400, "Room ID is required."))
                    .build();
        }

        if (room.getName() == null || room.getName().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiError(400, "Room name is required."))
                    .build();
        }

        if (room.getCapacity() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiError(400, "Room capacity must be greater than 0."))
                    .build();
        }

        if (DataStore.rooms.containsKey(room.getId())) {
            throw new DuplicateResourceException("A room with this ID already exists.");
        }

        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        DataStore.rooms.put(room.getId(), room);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(room.getId())
                .build();

        ApiResponse<Room> response = new ApiResponse<>(
                201,
                "Room created successfully.",
                room,
                buildRoomLinks(room)
        );

        return Response.created(location)
                .entity(response)
                .build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiError(404, "Room not found."))
                    .build();
        }

        ApiResponse<Room> response = new ApiResponse<>(
                200,
                "Room retrieved successfully.",
                room,
                buildRoomLinks(room)
        );

        return Response.ok(response).build();
    }

    @GET
    @Path("/{roomId}/sensors")
    public Response getSensorsByRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiError(404, "Room not found."))
                    .build();
        }

        List<Sensor> roomSensors = new ArrayList<>();

        if (room.getSensorIds() != null) {
            for (String sensorId : room.getSensorIds()) {
                Sensor sensor = DataStore.sensors.get(sensorId);

                if (sensor != null) {
                    roomSensors.add(sensor);
                }
            }
        }

        roomSensors.sort(Comparator.comparing(Sensor::getId));

        Map<String, String> links = new LinkedHashMap<>();
        links.put("self", "/api/v1/rooms/" + roomId + "/sensors");
        links.put("room", "/api/v1/rooms/" + roomId);

        CollectionResponse<Sensor> response = new CollectionResponse<>(
                200,
                "Sensors assigned to room retrieved successfully.",
                roomSensors,
                links
        );

        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiError(404, "Room not found."))
                    .build();
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                    "Cannot delete room. Sensors are still assigned to this room."
            );
        }

        DataStore.rooms.remove(roomId);

        Map<String, String> links = new LinkedHashMap<>();
        links.put("rooms", "/api/v1/rooms");

        ApiResponse<String> response = new ApiResponse<>(
                200,
                "Room deleted successfully.",
                roomId,
                links
        );

        return Response.ok(response).build();
    }
    
    private Map<String, String> buildRoomLinks(Room room) {
        Map<String, String> links = new LinkedHashMap<>();

        links.put("self", "/api/v1/rooms/" + room.getId());
        links.put("sensors", "/api/v1/rooms/" + room.getId() + "/sensors");
        links.put("rooms", "/api/v1/rooms");

        return links;
    }

}
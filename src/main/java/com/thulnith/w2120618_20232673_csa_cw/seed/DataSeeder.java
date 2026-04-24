/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.seed;

import com.thulnith.w2120618_20232673_csa_cw.model.Room;
import com.thulnith.w2120618_20232673_csa_cw.model.Sensor;
import com.thulnith.w2120618_20232673_csa_cw.model.SensorReading;
import com.thulnith.w2120618_20232673_csa_cw.store.DataStore;
import java.util.ArrayList;

/**
 *
 * @author Thulnith
 */
public class DataSeeder {

    private static boolean seeded = false;

    public static void seed() {
        if (seeded) {
            return;
        }

        Room room1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room room2 = new Room("LAB-201", "Computer Lab 201", 40);
        Room room3 = new Room("SEM-100", "Seminar Room 100", 25);

        DataStore.rooms.put(room1.getId(), room1);
        DataStore.rooms.put(room2.getId(), room2);
        DataStore.rooms.put(room3.getId(), room3);

        Sensor sensor1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 24.5, "LIB-301");
        Sensor sensor2 = new Sensor("CO2-001", "CO2", "ACTIVE", 420.0, "LAB-201");
        Sensor sensor3 = new Sensor("TEMP-777", "Temperature", "MAINTENANCE", 0.0, "LIB-301");

        DataStore.sensors.put(sensor1.getId(), sensor1);
        DataStore.sensors.put(sensor2.getId(), sensor2);
        DataStore.sensors.put(sensor3.getId(), sensor3);

        room1.getSensorIds().add(sensor1.getId());
        room2.getSensorIds().add(sensor2.getId());
        room1.getSensorIds().add(sensor3.getId());

        DataStore.readings.put(sensor1.getId(), new ArrayList<SensorReading>());
        DataStore.readings.put(sensor2.getId(), new ArrayList<SensorReading>());
        DataStore.readings.put(sensor3.getId(), new ArrayList<SensorReading>());

        seeded = true;
    }

    private DataSeeder() {
    }
}
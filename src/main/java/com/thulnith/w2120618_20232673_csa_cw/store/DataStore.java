/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.store;

import com.thulnith.w2120618_20232673_csa_cw.model.Room;
import com.thulnith.w2120618_20232673_csa_cw.model.Sensor;
import com.thulnith.w2120618_20232673_csa_cw.model.SensorReading;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Thulnith
 */
public class DataStore {

    public static final Map<String, Room> rooms = new HashMap<>();
    public static final Map<String, Sensor> sensors = new HashMap<>();
    public static final Map<String, List<SensorReading>> readings = new HashMap<>();

    private DataStore() {
    }
}
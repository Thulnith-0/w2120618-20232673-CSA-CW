/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.config;

import com.thulnith.w2120618_20232673_csa_cw.filter.LoggingFilter;
import com.thulnith.w2120618_20232673_csa_cw.mapper.BadRequestExceptionMapper;
import com.thulnith.w2120618_20232673_csa_cw.mapper.DuplicateResourceExceptionMapper;
import com.thulnith.w2120618_20232673_csa_cw.mapper.JsonMappingExceptionMapper;
import com.thulnith.w2120618_20232673_csa_cw.mapper.JsonParseExceptionMapper;
import com.thulnith.w2120618_20232673_csa_cw.mapper.LinkedResourceNotFoundExceptionMapper;
import com.thulnith.w2120618_20232673_csa_cw.mapper.RoomNotEmptyExceptionMapper;
import com.thulnith.w2120618_20232673_csa_cw.mapper.SensorUnavailableExceptionMapper;
import com.thulnith.w2120618_20232673_csa_cw.mapper.ThrowableExceptionMapper;
import com.thulnith.w2120618_20232673_csa_cw.mapper.WebApplicationExceptionMapper;
import com.thulnith.w2120618_20232673_csa_cw.resource.DiagnosticsResource;
import com.thulnith.w2120618_20232673_csa_cw.resource.DiscoveryResource;
import com.thulnith.w2120618_20232673_csa_cw.resource.RoomResource;
import com.thulnith.w2120618_20232673_csa_cw.resource.SensorResource;
import com.thulnith.w2120618_20232673_csa_cw.seed.DataSeeder;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Thulnith
 */
public class SmartCampusApplication extends ResourceConfig {
    
    public SmartCampusApplication() {
        DataSeeder.seed();

        register(DiscoveryResource.class);
        register(RoomResource.class);
        register(SensorResource.class);
        register(DiagnosticsResource.class);

        register(RoomNotEmptyExceptionMapper.class);
        register(LinkedResourceNotFoundExceptionMapper.class);
        register(SensorUnavailableExceptionMapper.class);
        register(DuplicateResourceExceptionMapper.class);
        register(BadRequestExceptionMapper.class);
        register(JsonParseExceptionMapper.class);
        register(JsonMappingExceptionMapper.class);
        register(WebApplicationExceptionMapper.class);
        register(ThrowableExceptionMapper.class);

        register(LoggingFilter.class);
    }
    
}
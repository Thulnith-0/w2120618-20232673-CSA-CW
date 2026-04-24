/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.mapper;

import com.thulnith.w2120618_20232673_csa_cw.exception.SensorUnavailableException;
import com.thulnith.w2120618_20232673_csa_cw.model.ApiError;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Thulnith
 */
@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
  
    @Override
    public Response toResponse(SensorUnavailableException ex) {
        ApiError error = new ApiError(403, ex.getMessage());

        return Response.status(403)
                .entity(error)
                .build();
    }
    
}

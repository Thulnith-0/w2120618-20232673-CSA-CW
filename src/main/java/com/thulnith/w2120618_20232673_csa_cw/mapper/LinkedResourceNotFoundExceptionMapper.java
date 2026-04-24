/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.mapper;

import com.thulnith.w2120618_20232673_csa_cw.exception.LinkedResourceNotFoundException;
import com.thulnith.w2120618_20232673_csa_cw.model.ApiError;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Thulnith
 */
@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    
    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        ApiError error = new ApiError(422, ex.getMessage());

        return Response.status(422)
                .entity(error)
                .build();
    }
    
}

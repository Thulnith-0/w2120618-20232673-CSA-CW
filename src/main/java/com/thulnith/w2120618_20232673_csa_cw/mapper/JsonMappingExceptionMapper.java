/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.mapper;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.thulnith.w2120618_20232673_csa_cw.model.ApiError;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Thulnith
 */
@Provider
public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {

    @Override
    public Response toResponse(JsonMappingException ex) {
        ApiError error = new ApiError(
                400,
                "Invalid request body or malformed JSON."
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .build();
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.mapper;

import com.thulnith.w2120618_20232673_csa_cw.model.ApiError;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Thulnith
 */
@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException ex) {
        int status = ex.getResponse() != null ? ex.getResponse().getStatus() : 500;

        String message;
        if (status == 404) {
            message = "Requested resource was not found.";
        } else if (status == 405) {
            message = "HTTP method not allowed for this endpoint.";
        } else if (status == 415) {
            message = "Unsupported media type. Use application/json.";
        } else if (status == 400) {
            message = "Bad request.";
        } else {
            message = ex.getMessage() != null && !ex.getMessage().trim().isEmpty()
                    ? ex.getMessage()
                    : "Request could not be processed.";
        }

        ApiError error = new ApiError(status, message);

        return Response.status(status)
                .entity(error)
                .build();
    }
}
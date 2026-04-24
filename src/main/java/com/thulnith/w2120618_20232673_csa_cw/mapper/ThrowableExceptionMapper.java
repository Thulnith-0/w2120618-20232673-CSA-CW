/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.mapper;

import com.thulnith.w2120618_20232673_csa_cw.model.ApiError;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Thulnith
 */
@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger logger = Logger.getLogger(ThrowableExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable ex) {
        logger.log(Level.SEVERE, "Unhandled server error", ex);

        ApiError error = new ApiError(
                500,
                "Internal server error. Please contact support."
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .build();
    }
}
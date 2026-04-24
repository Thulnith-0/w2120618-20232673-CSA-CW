/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Thulnith
 */
@Path("/diagnostics")
@Produces(MediaType.APPLICATION_JSON)
public class DiagnosticsResource {

    @GET
    @Path("/failure-test")
    public String failureTest() {
        throw new RuntimeException("Intentional diagnostics failure test.");
    }
}
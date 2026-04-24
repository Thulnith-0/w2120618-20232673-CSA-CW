/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thulnith.w2120618_20232673_csa_cw;

import com.thulnith.w2120618_20232673_csa_cw.config.SmartCampusApplication;
import com.thulnith.w2120618_20232673_csa_cw.seed.DataSeeder;
import java.net.URI;
import java.util.logging.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

/**
 *
 * @author Thulnith
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static final String BASE_URI = "http://localhost:8080/api/v1/";

    public static HttpServer startServer() {
        return GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI),
                new SmartCampusApplication()
        );
    }

    public static void main(String[] args) {
        DataSeeder.seed();

        HttpServer server = startServer();

        logger.info("Smart Campus API is running.");
        logger.info("Base URL: " + BASE_URI);
    }
}
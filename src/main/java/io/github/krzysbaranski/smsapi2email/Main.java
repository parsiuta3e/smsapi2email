package io.github.krzysbaranski.smsapi2email;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

/**
 * Main class.
 *
 */
public class Main {
    private static final String port = Optional.ofNullable(System.getenv("PORT")).orElse("8090");
    private static final String host = Optional.ofNullable(System.getenv("HOSTNAME")).orElse("localhost");

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://" + host + ":"+ port +"/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in krzysbaranski.github.io package
        final ResourceConfig rc = new ResourceConfig().packages("io.github.krzysbaranski.smsapi2email");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit ctrl+c to stop it...", BASE_URI));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down...");
            server.shutdownNow();
        }));
    }
}

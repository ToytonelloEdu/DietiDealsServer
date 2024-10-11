package org.example;

import org.example.data.DatabaseSession;
import org.example.data.repos.*;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://0.0.0.0:8080/api/1.0";

    private static Thread notifThread;

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("org.example");

        return GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI),
                rc

        );
    }

    private static void initRepositories(){
        ImagesFilesRepository.getInstance();
        BidsDbRepository.getInstance();
        TagsDbRepository.getInstance();

        UsersDbRepository usersRepo = UsersDbRepository.getInstance();
        PhotosDbRepository.getInstance(usersRepo);
        AuctionsDbRepository.getInstance();

        NotificationsDbRepository.getInstance();
        notifThread = NotificationsDbRepository.getPopulationThread();
    }

    /**
     * Main method.
     * @param args defaylt Main args
     * @throws IOException when Database connection fails
     */
    public static void main(String[] args) throws IOException {
        try{

            DatabaseSession.getSession();
            initRepositories();
        } catch (IllegalStateException e){
            System.out.println("Database session is invalid");
        }

        final HttpServer server = startServer();
        System.out.printf("Jersey app started with endpoints available at "
                             + "%s%nHit Ctrl-C to stop it...%n", BASE_URI);

        try {
            notifThread.join();
        } catch (InterruptedException e) {
            NotificationsDbRepository.stopNotifsPopulation();
        } finally {
            server.shutdownNow();
        }
    }


}


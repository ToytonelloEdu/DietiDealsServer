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

//    private static final String KEYSTORE_LOC = "./certificates/keystore_server";
//    private static final String KEYSTORE_PASS= "antojava";
//
//    private static final String TRUSTSTORE_LOC = "./certificates/truststore_server";
//    private static final String TRUSTSTORE_PASS = "antojava";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in org.example package
        final ResourceConfig rc = new ResourceConfig().packages("org.example");

//        SSLContextConfigurator sslCon = new SSLContextConfigurator();

//        sslCon.setKeyStoreFile(KEYSTORE_LOC);
//        sslCon.setKeyStorePass(KEYSTORE_PASS);
//
//        sslCon.setTrustStoreFile(TRUSTSTORE_LOC);
//        sslCon.setTrustStorePass(TRUSTSTORE_PASS);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI),
                rc
//                true,
//                new SSLEngineConfigurator(sslCon)
//                        .setClientMode(false)
//                        .setNeedClientAuth(false)
//                        .setWantClientAuth(false)
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
    }

    /**
     * Main method.
     * @param args defaylt Main args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        DatabaseSession.getSession();
        initRepositories();
        final HttpServer server = startServer();
        System.out.printf("Jersey app started with endpoints available at "
                             + "%s%nHit Ctrl-C to stop it...%n", BASE_URI);
        System.in.read();
        server.stop();
        NotificationsDbRepository.stopNotifsPopulation();
    }


}


package org.example.data;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.data.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

import java.util.function.Consumer;

public class DatabaseSession {
    static {
        Dotenv dotenv = Dotenv.load();
        USER = dotenv.get("DB_USER");
        PASSWORD = dotenv.get("DB_PASSWORD");
        DB_URL = dotenv.get("DB_URL");
    }
    private static final String USER;
    private static final String PASSWORD;
    private static final String DB_URL;

    public static final SessionFactory sessionFactory =
            new Configuration()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Auctioneer.class)
                    .addAnnotatedClass(Buyer.class)
                    .addAnnotatedClass(Links.class)
                    .addAnnotatedClass(Auction.class)
                    .addAnnotatedClass(IncrementalAuction.class)
                    .addAnnotatedClass(SilentAuction.class)
                    .addAnnotatedClass(Bid.class)
                    .addAnnotatedClass(Tag.class)
                    .addAnnotatedClass(AuctionPhoto.class)
                    .addAnnotatedClass(Notification.class)
                    // PostgreSQL
                    .setProperty(AvailableSettings.JAKARTA_JDBC_URL, DB_URL)
                    // Credentials
                    .setProperty(AvailableSettings.JAKARTA_JDBC_USER, USER)
                    .setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, PASSWORD)
                    // Automatic schema export
                    .setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION,
                            //Action.NONE
                            //Action.ACTION_UPDATE
                            Action.SPEC_ACTION_DROP_AND_CREATE
                    )
                    .setProperty(AvailableSettings.JAKARTA_JDBC_DRIVER, "org.postgresql.Driver")
                    // Create a new SessionFactory
                    .buildSessionFactory();


    public static Session getSession() {
        return sessionFactory.openSession();
    }


    public static void inTransaction(Consumer<Session> action) {
        sessionFactory.inTransaction(action);
    }
}

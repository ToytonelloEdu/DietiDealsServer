package org.example.data;

import org.example.data.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

public class DatabaseSession {
    private static final String user = "postgres";
    private static final String password = "sangio";
            //"dd_dealsdieti_p455w0rd!24";

    public static final SessionFactory sessionFactory =
            new Configuration()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Auctioneer.class)
                    .addAnnotatedClass(Buyer.class)
                    .addAnnotatedClass(Auction.class)
                    .addAnnotatedClass(IncrementalAuction.class)
                    .addAnnotatedClass(SilentAuction.class)
                    .addAnnotatedClass(Bid.class)
                    .addAnnotatedClass(Tag.class)
                    .addAnnotatedClass(AuctionPhoto.class)
                    // PostgreSQL
                    .setProperty(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:postgresql://localhost:5433/dietideals")
                    // Credentials
                    .setProperty(AvailableSettings.JAKARTA_JDBC_USER, user)
                    .setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, password)
                    // Automatic schema export
                    .setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION,
                            //Action.NONE
                            Action.ACTION_UPDATE
                            //Action.SPEC_ACTION_DROP_AND_CREATE
                    )
                    // SQL statement logging
                    .setProperty(AvailableSettings.SHOW_SQL, true)
                    .setProperty(AvailableSettings.FORMAT_SQL, true)
                    .setProperty(AvailableSettings.HIGHLIGHT_SQL, true)
                    // Create a new SessionFactory
                    .buildSessionFactory();

    private static Session session;

    public static Session getSession() {
//        if (session == null) {
//            session = sessionFactory.openSession();
//        }
//        return session;
        return sessionFactory.openSession();
    }
}

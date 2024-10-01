package org.example.repos;

import org.example.data.entities.*;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDbRepositoryTest {



    //Session session = DatabaseSession.getSession();

    @Test
    public void TestNotificationsDbPopulation() {
        Auctioneer toytonello = new Auctioneer(
                "toytonello",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                new Timestamp(System.currentTimeMillis())
        );
        Auction auction = new IncrementalAuction(
                        1,
                        "test",
                        "test",
                        new Timestamp(System.currentTimeMillis()),
                        toytonello,
                        null,
                        100,
                        100.0,
                        100.0
                );
        auction.setBids(new ArrayList<>());
        auction.getBids().add(
                new Bid(
                        0,
                        auction,
                        new Buyer("ciroanastasio", "", "","","","","","","","", new Timestamp(System.currentTimeMillis())),
                        100.0,
                        new Timestamp(System.currentTimeMillis())
                )
        );
        List<Notification> notifications = Notification.auctionNotifications(auction);
        for (Notification notification : notifications) {
            System.out.println(notification);
        }

    }

}

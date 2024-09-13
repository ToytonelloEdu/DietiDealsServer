package org.example.data.repos;

import org.example.data.DatabaseSession;
import org.example.data.entities.Auction;
import org.example.data.entities.Notification;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class NotificationsDbRepository implements NotificationsRepository {
    private static volatile NotificationsDbRepository instance;
    private static volatile AuctionsRepository auctionsRepo;

    private static volatile Thread population = null;

    //TODO: Change to map and implement so that changes do update this Map as well
    public static volatile List<Auction> auctions = new ArrayList<>();

    private NotificationsDbRepository() {
        auctionsRepo = AuctionsDbRepository.getInstance();
        auctions = auctionsRepo.getAuctionsNotNotified();
        startNotifsPopulation();
    }

    public static NotificationsDbRepository getInstance() {
        if (instance == null) {
            instance = new NotificationsDbRepository();
        }
        return instance;
    }

    public static Thread getPopulationThread() {
        return population;
    }

    private static void startNotifsPopulation() {
        population = new Thread(NotificationsDbRepository::notifsPopulationCycle);
        population.start();
    }

    public static void stopNotifsPopulation() {
        if (population != null) {
            population.interrupt();
        }
    }

    private static void notifsPopulationCycle() {
        boolean interrupted = false;
        while (!interrupted) {
            System.out.println("Cycle start+++++++++++++++++++++++++++++++++++++++++++++++");
            for (int i = 0, auctionsSize = auctions.size(); i < auctionsSize; i++) {
                Auction updatedAuction = updateAuction(i);
                if (updatedAuction != null && updatedAuction.auctionOver()) {
                    notifyAuction(updatedAuction);
                }
            }
            try{Thread.sleep(5000);}catch(InterruptedException ignored){interrupted = true;}

        }
    }

    private static void notifyAuction(Auction updatedAuction) {
        Session session = DatabaseSession.getSession();

        session.beginTransaction();

            session.evict(updatedAuction);

            List<Notification> notifications = Notification.auctionNotifications(updatedAuction);
            for(Notification notification : notifications) session.persist(notification);

            updatedAuction.notified(true);
            session.merge(updatedAuction);
            auctions.remove(updatedAuction);

        session.getTransaction().commit();
    }

    private static Auction updateAuction(int i) {
        int id = auctions.get(i).getId();
        auctions.set(i, DatabaseSession.getSession().find(Auction.class, id));
        return auctions.get(i);
    }

}

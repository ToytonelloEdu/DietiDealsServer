package org.example.data.repos;

import org.example.data.DatabaseSession;
import org.example.data.entities.Auction;
import org.example.data.entities.Notification;
import org.hibernate.Session;

import java.util.List;

public class NotificationsDbRepository implements NotificationsRepository {
    private static volatile NotificationsDbRepository instance;
    private static volatile AuctionsRepository auctionsRepo;

    private static volatile Thread population = null;

    //TODO: Implement methods on BlockingQueue, every insert in AuctionsDbRepository adds the auction to this queue
    protected static volatile List<Auction> auctions = null;

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
            for (int i = 0, auctionsSize = auctions.size(); i < auctionsSize; i++) {
                Auction updatedAuction = updateAuction(i);
                if (updatedAuction != null && updatedAuction.isOver()) {
                    notifyAuction(updatedAuction);
                }
            }
            try{Thread.sleep(1000);}catch(InterruptedException ignored){interrupted = true;}
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
        auctions.set(i, auctionsRepo.getAuctionByID(id));
        return auctions.get(i);
    }

}

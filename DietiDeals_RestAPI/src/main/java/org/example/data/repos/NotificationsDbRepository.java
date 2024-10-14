package org.example.data.repos;

import org.example.data.DatabaseSession;
import org.example.data.entities.Auction;
import org.example.data.entities.Bid;
import org.example.data.entities.Notification;
import org.example.data.entities.User;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class NotificationsDbRepository implements NotificationsRepository {
    private static NotificationsDbRepository instance;
    private static UsersRepository usersRepo;

    private static volatile Thread population = null;

    protected static final List<Auction> auctions = new ArrayList<>();

    private NotificationsDbRepository() {
        usersRepo = UsersDbRepository.getInstance();
        AuctionsRepository auctionsRepo = AuctionsDbRepository.getInstance();
        auctions.addAll(auctionsRepo.getAuctionsNotNotified());
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

    @Override
    public List<Notification> getNotificationsByUser(String handle) {
        System.out.println("\nSELECT * FROM Notification WHERE user = ?\n");
        User user = usersRepo.getUserByHandle(handle);
        List<Notification> notifications = DatabaseSession.getSession()
                .createSelectionQuery("FROM Notification WHERE user = :user", Notification.class)
                .setParameter("user", user).getResultList();

        for(Notification notification : notifications) {
            notification.setAuction(notification.getAuction().toHomeJsonFriendly());
            Bid jsonBid = notification.getAuction().getLastBid().toJsonFriendly();
            notification.getAuction().setLastBid(jsonBid);
            notification.setUser(notification.getUser().toJsonFriendly());
        }

        return notifications;
    }




    private static void startNotifsPopulation() {
        synchronized (NotificationsDbRepository.class) {
            population = new Thread(NotificationsDbRepository::notifsPopulationCycle);
        }
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
                synchronized (auctions) {
                    Auction updatedAuction = fetchAuctionUpToDate(i);
                    if (updatedAuction != null && updatedAuction.auctionOver()) {
                        notifyAuction(updatedAuction);
                    }
                }
            }
            try{Thread.sleep(5000);}catch(InterruptedException ignored){
                interrupted = true;
            }

        }
    }

    private static void notifyAuction(Auction updatedAuction) {
        try {
            Session session = DatabaseSession.getSession();

            session.beginTransaction();

            session.evict(updatedAuction);

            List<Notification> notifications = Notification.auctionNotifications(updatedAuction);
            for(Notification notification : notifications) session.persist(notification);

            updatedAuction.notified(true);
            session.merge(updatedAuction);
            auctions.remove(updatedAuction);


            session.getTransaction().commit();
            System.out.println("\nAuction " + updatedAuction.getId() + " has been notified\n");
        } catch (Exception e) {
            System.out.println("\nAuction " + updatedAuction.getId() + " could not be notified\n" +
                                    "CAUSE: " + e.getMessage());
        }

    }

    private static Auction fetchAuctionUpToDate(int i) {
        int id = auctions.get(i).getId();
        auctions.set(i, DatabaseSession.getSession().find(Auction.class, id));
        return auctions.get(i);
    }

}

package org.example.data.repos;

import org.example.auth.AuthCredentials;
import org.example.data.entities.*;
import org.hibernate.Session;

import static org.example.data.DatabaseSession.sessionFactory;

public class UsersDbRepository implements UsersRepository {
    private static UsersDbRepository instance;

    private UsersDbRepository() {}

    public static UsersDbRepository getInstance() {
        if (instance == null) {
            instance = new UsersDbRepository();
        }
        return instance;
    }

    @Override
    public User getUserByUsername(String username) {
        User subUser = sessionFactory.openSession().find(User.class, username);
        if(subUser.getUserType().equals("Auctioneer")) {
            Auctioneer auctioneer = new Auctioneer(subUser);
            auctioneer.setAuctions(
                    sessionFactory.openSession()
                        .createSelectionQuery("FROM Auction WHERE auctioneer = :auctioneer", Auction.class)
                        .setParameter("auctioneer", auctioneer).getResultList()
            );
            return auctioneer;
        } else {
            Buyer buyer = new Buyer(subUser);
            buyer.setBids(
                    sessionFactory.openSession()
                        .createSelectionQuery("FROM Bid WHERE buyer = :buyer", Bid.class)
                        .setParameter("buyer", buyer).getResultList()
            );
            return buyer;
        }
    }


    @Override
    public User addUser(User user) {
        User subUser;
        try {
            if(user.getUserType().equals("Auctioneer")) {
                subUser = new Auctioneer(user);
            } else if(user.getUserType().equals("Buyer")) {
                subUser = new Buyer(user);
            } else throw new IllegalArgumentException("Invalid user type");
            sessionFactory.inTransaction(session -> {
                session.persist(subUser);
            });
            return user;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public Boolean verifyCredentials(AuthCredentials auth) {
        User user = sessionFactory.openSession().find(User.class, auth.getUsername());
        return user.checkCredentials(auth);
    }

}

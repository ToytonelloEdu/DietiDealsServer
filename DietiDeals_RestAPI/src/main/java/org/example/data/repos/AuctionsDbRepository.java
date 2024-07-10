package org.example.data.repos;

import org.example.data.entities.Auction;
import org.hibernate.Session;

import java.util.List;

import static org.example.data.DatabaseSession.sessionFactory;

public class AuctionsDbRepository implements AuctionsRepository {
    private static AuctionsDbRepository instance;

    private AuctionsDbRepository() {}

    public static AuctionsDbRepository getInstance() {
        if (instance == null) {
            instance = new AuctionsDbRepository();
        }
        return instance;
    }

    @Override
    public List<Auction> getAuctions() {
        return sessionFactory.openSession()
                .createQuery("FROM Auction", Auction.class).getResultList();
    }

    @Override
    public Auction getAuctionByID(int id) {
        return sessionFactory.openSession().getReference(Auction.class, id);
    }

    @Override
    public Auction addAuction(Auction auction) {
        //TODO: Check documentation for correct design
        sessionFactory.inTransaction(session -> {
            session.persist(auction);
            session.flush();
        });
        return auction;
    }

    @Override
    public Auction deleteAuction(int id) {
        Auction auction = getAuctionByID(id);
        System.out.println(auction.toString());
        return auction;
    }


}

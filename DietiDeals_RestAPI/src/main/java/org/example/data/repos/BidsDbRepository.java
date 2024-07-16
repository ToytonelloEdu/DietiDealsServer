package org.example.data.repos;

import org.example.data.entities.Auction;
import org.example.data.entities.Bid;
import org.example.data.entities.Buyer;

import java.util.List;

import static org.example.data.DatabaseSession.sessionFactory;

public class BidsDbRepository implements BidsRepository {
    private static BidsDbRepository instance;

    private BidsDbRepository() {}

    public static BidsDbRepository getInstance() {
        if (instance == null) {
            instance = new BidsDbRepository();
        }
        return instance;
    }


    @Override
    public List<Bid> getBidsByAuction(Auction auction) {
        return sessionFactory.openSession()
                .createSelectionQuery("SELECT new org.example.data.entities.Bid(id, buyer, amount) FROM Bid WHERE auction = :auction", Bid.class)
                .setParameter("auction", auction).getResultList();
    }

    @Override
    public List<Bid> getBidsByUser(Buyer buyer) {
        return null;
    }
}

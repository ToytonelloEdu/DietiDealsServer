package org.example.data.repos;

import org.example.data.DatabaseSession;
import org.example.data.entities.Auction;
import org.example.data.entities.Bid;
import org.example.data.entities.Buyer;

import java.util.List;

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
        return DatabaseSession.getSession()
                .createSelectionQuery("SELECT new org.example.data.entities.Bid(id, buyer, amount, time) " +
                                         "FROM Bid WHERE auction = :auction", Bid.class)
                .setParameter("auction", auction).getResultList();
    }

    @Override
    public List<Bid> getBidsByUser(Buyer buyer) {
        return DatabaseSession.getSession()
                .createSelectionQuery("SELECT new org.example.data.entities.Bid(id, auction, amount, time) " +
                                         "FROM Bid WHERE buyer = :buyer", Bid.class)
                .setParameter("buyer", buyer).getResultList();
    }
}

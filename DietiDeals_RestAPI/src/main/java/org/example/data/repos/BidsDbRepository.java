package org.example.data.repos;

import org.example.data.DatabaseSession;
import org.example.data.entities.Auction;
import org.example.data.entities.Bid;
import org.example.data.entities.Buyer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return DatabaseSession.getSession()
                .createSelectionQuery("SELECT new org.example.data.entities.Bid(id, buyer, amount, time) " +
                        "FROM Bid WHERE auction = :auction ORDER BY time ASC", Bid.class)
                .setParameter("auction", auction).getResultList();
    }

    @Override
    public List<Bid> getBidsByUser(Buyer buyer) {
        List<Bid> bids = DatabaseSession.getSession()
                .createSelectionQuery("SELECT new org.example.data.entities.Bid(id, auction, amount, time) " +
                        "FROM Bid WHERE buyer = :buyer ORDER BY time ASC", Bid.class)
                .setParameter("buyer", buyer).getResultList();
        Map<Integer, Bid> map = new HashMap<>();
        for (Bid bid : bids) {
            map.put(bid.getAuction().getId(), bid);
        }
        List<Bid> ret = new ArrayList<>(map.values()); ret.sort(Bid.compareByTimeDesc);
        return ret;
    }

    @Override
    public Bid addBid(Bid bid) throws IllegalArgumentException {
        if (bid == null) throw  new IllegalArgumentException("bid is null");
        try {
            sessionFactory.inTransaction(session -> {
                Auction auction = session.find(Auction.class, bid.auctionId());
                bid.setAuction(auction);

                Buyer buyer = session.find(Buyer.class, bid.getBuyer().getUsername());
                bid.setBuyer(buyer);

                session.persist(bid);
            });
            return bid;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}

package org.example.data.repos;

import org.example.data.entities.Auction;
import org.example.data.entities.Auctioneer;

import java.util.List;

import static org.example.data.DatabaseSession.sessionFactory;

public class AuctionsDbRepository implements AuctionsRepository {
    private static AuctionsDbRepository instance;
    private static BidsRepository bidsRepo;

    private AuctionsDbRepository() {
        bidsRepo = BidsDbRepository.getInstance();
    }

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
        return sessionFactory.openSession().find(Auction.class, id);
    }

    public List<Auction> getAuctionsByAuctioneer(Auctioneer auctioneer) {
        List<Auction> auctions = getAuctionsThroughQuery_where(auctioneer);
        for (Auction auction : auctions) {
            //auction.setAuctioneerUsername(auctioneer.getUsername());
            auction.setBids(bidsRepo.getBidsByAuction(auction));
        }
        return auctions;
    }

    private List<Auction> getAuctionsThroughQuery_where(Auctioneer auctioneer) {
        return sessionFactory.openSession()
                .createSelectionQuery("select new org.example.data.entities.Auction(id, picturePath, objectName, description, date) FROM Auction WHERE auctioneer = :auctioneer", Auction.class)
                .setParameter("auctioneer", auctioneer).getResultList();
    }

    @Override
    public Auction addAuction(Auction auction) {
        sessionFactory.inTransaction(session -> {
            session.persist(auction);
        });
        return auction;
    }

    @Override
    public Auction deleteAuction(int id) {
        Auction auction = getAuctionByID(id);
        sessionFactory.inTransaction(session -> {
            session.detach(auction);
        });
        return auction;
    }


}

package org.example.data.repos;

import org.example.data.entities.Auction;
import org.example.data.entities.Auctioneer;

import java.util.List;

import static org.example.data.DatabaseSession.sessionFactory;

public class AuctionsDbRepository implements AuctionsRepository {
    private static AuctionsDbRepository instance;
    private static BidsRepository bidsRepo;
    private static TagsRepository tagsRepo;

    private AuctionsDbRepository() {
        bidsRepo = BidsDbRepository.getInstance();
        tagsRepo = TagsDbRepository.getInstance();
    }

    public static AuctionsDbRepository getInstance() {
        if (instance == null) {
            instance = new AuctionsDbRepository();
        }
        return instance;
    }

    @Override
    public List<Auction> getAuctions() {
        List<Auction> auctions = getAuctionsThroughQuery();
        for (Auction auction : auctions) {
            auction.retrieveLastBid(bidsRepo.getBidsByAuction(auction));
            auction.setTags(tagsRepo.getTagsByAuction(auction));
        }
        return auctions;
    }



    @Override
    public Auction getAuctionByID(int id) {
        Auction auction = getAuctionThroughQuery_where(id);
        auction.setBids(bidsRepo.getBidsByAuction(auction));
        auction.setTags(tagsRepo.getTagsByAuction(auction));
        return auction;
    }

    public List<Auction> getAuctionsByAuctioneer(Auctioneer auctioneer) {
        List<Auction> auctions = getAuctionsThroughQuery_where(auctioneer);
        for (Auction auction : auctions) {
            auction.setBids(bidsRepo.getBidsByAuction(auction));
        }
        return auctions;
    }

    private List<Auction> getAuctionsThroughQuery() {
        return sessionFactory.openSession()
                .createSelectionQuery("select new org.example.data.entities.Auction(id, picturePath, objectName, description, auctioneer, date) FROM Auction", Auction.class)
                .getResultList();
    }

    private Auction getAuctionThroughQuery_where(Integer id) {
        return sessionFactory.openSession()
                .createSelectionQuery("select new org.example.data.entities.Auction(id, picturePath, objectName, description, date, auctioneer) FROM Auction WHERE id = :id", Auction.class)
                .setParameter("id", id).getSingleResult();
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

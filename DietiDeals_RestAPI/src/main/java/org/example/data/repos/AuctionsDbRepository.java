package org.example.data.repos;

import org.example.data.entities.Auction;
import org.example.data.entities.Auctioneer;
import org.example.data.entities.IncrementalAuction;
import org.example.data.entities.SilentAuction;

import java.util.ArrayList;
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
            if(auction.getAuctionType().equals("IncrementalAuction")){
                auction.setLastBid_fromList(bidsRepo.getBidsByAuction(auction));
            }
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
            auction.updateLastBid();
            auction.setTags(tagsRepo.getTagsByAuction(auction));
        }
        return auctions;
    }

    @Override
    public Auction addAuction(Auction auction) {
        if(auction instanceof IncrementalAuction){
           return addIncrAuction((IncrementalAuction) auction);
        } else if(auction instanceof SilentAuction){
           return addSilentAuction((SilentAuction) auction);
        } else throw new IllegalArgumentException("Unsupported auction type: " + auction.getClass());
    }

    protected Auction addIncrAuction(IncrementalAuction auction) {
        sessionFactory.inTransaction(session -> {
            session.persist(auction);
        });
        return auction;
    }

    protected Auction addSilentAuction(SilentAuction auction) {
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

    private List<Auction> getAuctionsThroughQuery() {
        List<Auction> auctions = new ArrayList<>();
        auctions.addAll(getIncrAuctionsThroughQuery());
        auctions.addAll(getSilentAuctionsThroughQuery());
        auctions.sort(Auction.ComparatorByDate);
        return auctions;
    }

    private List<IncrementalAuction> getIncrAuctionsThroughQuery() {
        return sessionFactory.openSession()
                .createSelectionQuery("select new org.example.data.entities.IncrementalAuction" +
                        "(id, picturePath, objectName, description, auctioneer, date, timeInterval, startingPrice, raisingThreshold) " +
                        "FROM IncrementalAuction ", IncrementalAuction.class)
                .getResultList();
    }

    private List<SilentAuction> getSilentAuctionsThroughQuery() {
        return sessionFactory.openSession()
                .createSelectionQuery("select new org.example.data.entities.SilentAuction" +
                        "(id, picturePath, objectName, description, auctioneer, date, expirationDate) " +
                        "FROM SilentAuction ", SilentAuction.class)
                .getResultList();
    }

    private Auction getAuctionThroughQuery_where(Integer id) {
        Auction auction = getIncrAuctionsThroughQuery_where(id);
        if (auction == null) {
            auction = getSilentAuctionsThroughQuery_where(id);
        }
        return auction;
    }

    private IncrementalAuction getIncrAuctionsThroughQuery_where(Integer id) {
        return sessionFactory.openSession()
                .createSelectionQuery("select new org.example.data.entities.IncrementalAuction" +
                        "(id, picturePath, objectName, description, date, auctioneer, timeInterval, startingPrice, raisingThreshold) " +
                        "FROM IncrementalAuction WHERE id = :id", IncrementalAuction.class)
                .setParameter("id", id).getSingleResultOrNull();
    }

    private SilentAuction getSilentAuctionsThroughQuery_where(Integer id) {
        return sessionFactory.openSession()
                .createSelectionQuery("select new org.example.data.entities.SilentAuction" +
                        "(id, picturePath, objectName, description, date, auctioneer, expirationDate) " +
                        "FROM SilentAuction WHERE id = :id", SilentAuction.class)
                .setParameter("id", id).getSingleResultOrNull();
    }

    private List<Auction> getAuctionsThroughQuery_where(Auctioneer auctioneer) {
        List<Auction> auctions = new ArrayList<>();
        auctions.addAll(getIncrAuctionsThroughQuery_where(auctioneer));
        auctions.addAll(getSilentAuctionsThroughQuery_where(auctioneer));
        return auctions;
    }

    private List<IncrementalAuction> getIncrAuctionsThroughQuery_where(Auctioneer auctioneer) {
        return sessionFactory.openSession()
                .createSelectionQuery("select new org.example.data.entities.IncrementalAuction" +
                        "(id, picturePath, objectName, description, auctioneer, date, timeInterval, startingPrice, raisingThreshold) " +
                        "FROM IncrementalAuction WHERE auctioneer = :auctioneer", IncrementalAuction.class)
                .setParameter("auctioneer", auctioneer).getResultList();
    }

    private List<SilentAuction> getSilentAuctionsThroughQuery_where(Auctioneer auctioneer) {
        return sessionFactory.openSession()
                .createSelectionQuery("select new org.example.data.entities.SilentAuction" +
                        "(id, picturePath, objectName, description, auctioneer, date, expirationDate) " +
                        "FROM SilentAuction WHERE auctioneer = :auctioneer", SilentAuction.class)
                .setParameter("auctioneer", auctioneer).getResultList();
    }
}

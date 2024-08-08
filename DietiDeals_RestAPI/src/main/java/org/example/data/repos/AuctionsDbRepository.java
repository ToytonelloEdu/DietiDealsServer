package org.example.data.repos;

import org.example.data.DatabaseSession;
import org.example.data.entities.*;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

import static org.example.data.DatabaseSession.sessionFactory;

public class AuctionsDbRepository implements AuctionsRepository {
    private static AuctionsDbRepository instance;
    private static BidsRepository bidsRepo;
    private static TagsRepository tagsRepo;
    private static PhotosRepository photosRepo;

    private AuctionsDbRepository() {
        bidsRepo = BidsDbRepository.getInstance();
        tagsRepo = TagsDbRepository.getInstance();
        photosRepo = PhotosDbRepository.getInstance();
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
            auction.setPictures(photosRepo.getPhotosByAuction(auction));
        }
        return auctions;
    }

    @Override
    public Auction getAuctionByID(int id) {
        Auction auction = getAuctionThroughQuery_where(id);
        auction.setBids(bidsRepo.getBidsByAuction(auction));
        auction.setTags(tagsRepo.getTagsByAuction(auction));
        auction.setPictures(photosRepo.getPhotosByAuction(auction));
        return auction;
    }

    public List<Auction> getAuctionsByAuctioneer(Auctioneer auctioneer) {
        List<Auction> auctions = getAuctionsThroughQuery_where(auctioneer);
        for (Auction auction : auctions) {
            auction.setBids(bidsRepo.getBidsByAuction(auction));
            auction.updateLastBid();
            auction.setTags(tagsRepo.getTagsByAuction(auction));
            auction.setPictures(photosRepo.getPhotosByAuction(auction));
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

    @Override
    public Auction updateAuction(Auction auction) {
        if(auction == null) throw new IllegalArgumentException("Auction is null");
        try {
            sessionFactory.inTransaction(session -> {
                session.merge(auction);
            });
            return auction;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to update auction: " + auction, e);
        }
    }

    protected Auction addIncrAuction(IncrementalAuction auction) {
        sessionFactory.inTransaction(session -> {
            List<Tag> tags = getPesistedTags(auction, session);
            auction.setTags(tags);

            session.persist(auction);
            System.out.println("Auction created: " + auction);
        });
        return auction;
    }

    protected Auction addSilentAuction(SilentAuction auction) {
        try {
            sessionFactory.inTransaction(session -> {
                List<Tag> tags = getPesistedTags(auction, session);
                auction.setTags(tags);

                session.persist(auction);
                System.out.println("Auction created: " + auction);
            });
        }
        catch (Exception e) {
            System.out.println("Error creating auction: " + e.getMessage());
            return null;
        }
        return auction;
    }

    private List<Tag> getPesistedTags(Auction auction, Session session) {
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : auction.getTags()) {
            Tag found = session.find(Tag.class, tag.getTagName());
            if (found != null) {
                tags.add(found);
            } else {
                tags.add(tag);
            }
        }
        return tags;
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
        return DatabaseSession.getSession()
                .createSelectionQuery("select new org.example.data.entities.IncrementalAuction" +
                        "(id, objectName, description, auctioneer, date, medianColor, timeInterval, startingPrice, raisingThreshold) " +
                        "FROM IncrementalAuction ", IncrementalAuction.class)
                .getResultList();
    }

    private List<SilentAuction> getSilentAuctionsThroughQuery() {
        return DatabaseSession.getSession()
                .createSelectionQuery("select new org.example.data.entities.SilentAuction" +
                        "(id, objectName, description, auctioneer, date, medianColor, expirationDate) " +
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
        return DatabaseSession.getSession()
                .createSelectionQuery("select new org.example.data.entities.IncrementalAuction" +
                        "(id, objectName, description, date, auctioneer, medianColor, timeInterval, startingPrice, raisingThreshold) " +
                        "FROM IncrementalAuction WHERE id = :id", IncrementalAuction.class)
                .setParameter("id", id).getSingleResultOrNull();
    }

    private SilentAuction getSilentAuctionsThroughQuery_where(Integer id) {
        return DatabaseSession.getSession()
                .createSelectionQuery("select new org.example.data.entities.SilentAuction" +
                        "(id, objectName, description, date, auctioneer, medianColor, expirationDate) " +
                        "FROM SilentAuction WHERE id = :id", SilentAuction.class)
                .setParameter("id", id).getSingleResultOrNull();
    }

    private List<Auction> getAuctionsThroughQuery_where(Auctioneer auctioneer) {
        List<Auction> auctions = new ArrayList<>();
        auctions.addAll(getIncrAuctionsThroughQuery_where(auctioneer));
        auctions.addAll(getSilentAuctionsThroughQuery_where(auctioneer));
        auctions.sort(Auction.ComparatorByDate);
        return auctions;
    }

    private List<IncrementalAuction> getIncrAuctionsThroughQuery_where(Auctioneer auctioneer) {
        return DatabaseSession.getSession()
                .createSelectionQuery("select new org.example.data.entities.IncrementalAuction" +
                        "(id, objectName, description, auctioneer, date, medianColor, timeInterval, startingPrice, raisingThreshold) " +
                        "FROM IncrementalAuction WHERE auctioneer = :auctioneer", IncrementalAuction.class)
                .setParameter("auctioneer", auctioneer).getResultList();
    }

    private List<SilentAuction> getSilentAuctionsThroughQuery_where(Auctioneer auctioneer) {
        return DatabaseSession.getSession()
                .createSelectionQuery("select new org.example.data.entities.SilentAuction" +
                        "(id, objectName, description, auctioneer, date, medianColor, expirationDate) " +
                        "FROM SilentAuction WHERE auctioneer = :auctioneer", SilentAuction.class)
                .setParameter("auctioneer", auctioneer).getResultList();
    }
}

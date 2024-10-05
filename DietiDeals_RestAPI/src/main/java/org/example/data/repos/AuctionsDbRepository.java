package org.example.data.repos;

import org.example.data.DatabaseSession;
import org.example.data.entities.*;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;

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
        System.out.println("\nSELECT * FROM Auctions\n");
        return auctions;
    }

    @Override
    public List<Auction> getAuctionsQueried(Query query) {
        String select = getQueryString(query);
        System.out.println(select);

        Session session = DatabaseSession.getSession();
        SelectionQuery<Auction> selectionQuery = session
                .createSelectionQuery(select ,Auction.class);

        if(query.object != null) selectionQuery.setParameter("object", query.object+"%");
        if(query.vendor != null) selectionQuery.setParameter("vendor", session.find(Auctioneer.class, query.vendor));
        if(!query.tags.isEmpty()) {
            List<String> strings = query.tags;
            for (int i = 0; i < strings.size(); i++) {
                String tagName = strings.get(i);
                Tag tag = session.find(Tag.class, tagName);
                selectionQuery.setParameter("tag"+(i+1), tag);
            }

        }

        List<Auction> auctions = selectionQuery.getResultList();
        auctions.replaceAll(Auction::toHomeJsonFriendly);
        auctions.sort(Auction.ComparatorByDate);
        System.out.println("\nSELECT * "+ select +"\n");
        return auctions;
    }

    private static String getQueryString(Query query) {
        StringBuilder stringBuilder = new StringBuilder("FROM Auction WHERE ");

        if(query.object != null){
            stringBuilder.append("objectName LIKE :object ");
            if(query.vendor != null){stringBuilder.append("AND ");}
        }

        if(query.vendor != null) stringBuilder.append("auctioneer = :vendor ");

        if(!query.tags.isEmpty()) {
            if (query.vendor != null || query.object != null) {stringBuilder.append("AND (");}
            List<String> tags = query.tags;
            for (int i = 0; i < tags.size(); i++) {
                stringBuilder.append(":tag").append(i+1).append(" MEMBER OF tags");
                if(tags.size() > i + 1)
                    stringBuilder.append(" OR ");
            }
            if (query.vendor != null || query.object != null) stringBuilder.append(")");
        }

        return stringBuilder.toString();
    }

    @Override
    public Auction getAuctionByID(int id) {
        Auction auction = getAuctionThroughQuery_where(id);
        auction.setBids(bidsRepo.getBidsByAuction(auction));
        auction.setTags(tagsRepo.getTagsByAuction(auction));
        auction.setPictures(photosRepo.getPhotosByAuction(auction));
        auction.getAuctioneer().setAuctions(null);
        System.out.println("\nSELECT * FROM Auctions WHERE id = ?\n");
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
        System.out.println("\nSELECT * FROM Auctions WHERE auctioneer = ?\n");
        return auctions;
    }

    @Override
    public List<Auction> getAuctionsNotNotified() {
        return DatabaseSession.getSession()
                .createSelectionQuery("FROM Auction WHERE notified = false", Auction.class)
                .getResultList();
    }

    @Override
    public Auction addAuction(Auction auction) {
        if(auction instanceof IncrementalAuction){
            System.out.println("\nINSERT (?, ?, ...,?) INTO Auction\n");
           return addIncrAuction((IncrementalAuction) auction);
        } else if(auction instanceof SilentAuction){
            System.out.println("\nINSERT (?, ?, ...,?) INTO Auction\n");
           return addSilentAuction((SilentAuction) auction);
        } else throw new IllegalArgumentException(auction == null ? "Auction is null" : "Unsupported auction type: " + auction.getClass());
    }

    @Override
    public Auction updateAuction(Auction auction) {
        if(auction == null) throw new IllegalArgumentException("Auction is null");

        try {
            sessionFactory.inTransaction(session -> {
                session.merge(auction);
            });
            System.out.println("\nUPDATE Auction SET ..... WHERE id = ?\n");
            return auction;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to update auction: " + auction, e);
        }
    }

    protected Auction addIncrAuction(IncrementalAuction auction) {
        try {
            sessionFactory.inTransaction(session -> {
                List<Tag> tags = getPesistedTags(auction, session);
                auction.setTags(tags);

                session.persist(auction);
                System.out.println("Auction created: " + auction);
            });
            NotificationsDbRepository.auctions.add(auction);
        } catch (Exception e) {
            System.out.println("Error creating auction: " + e.getMessage());
            return null;
        }

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
            NotificationsDbRepository.auctions.add(auction);
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
            session.remove(auction);
        });
        System.out.println("\nDELETE FROM Auction WHERE id = ?\n");
        return auction;
    }

    @Override
    public Auction acceptBid(int auctionId, int bidId) {
        Session session = DatabaseSession.getSession();
        Auction auction = session.find(Auction.class, auctionId);

        if(auction == null) throw new IllegalArgumentException("Auction does not exist");
        if(!(auction instanceof SilentAuction)) throw new IllegalArgumentException("Auction is not Silent");
        SilentAuction silentAuction = (SilentAuction) auction;
        if(silentAuction.getAcceptedBid() != null) throw new IllegalArgumentException("Auction has already an accepted bid");

        Bid acceptedBid = session.find(Bid.class, bidId);
        if(acceptedBid == null) throw new IllegalArgumentException("Bid does not exist");
        if(acceptedBid.auctionId() != auctionId) throw new IllegalArgumentException("Bid was not done for selected Auction");


        session.beginTransaction();
            silentAuction.setAcceptedBid(acceptedBid);
            Auction merged = session.merge(silentAuction);
        session.getTransaction().commit();
        return merged;
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
                        "(id, objectName, description, auctioneer, date, medianColor, expirationDate, acceptedBid) " +
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
                        "(id, objectName, description, date, auctioneer, medianColor, expirationDate, acceptedBid) " +
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
                        "(id, objectName, description, auctioneer, date, medianColor, expirationDate, acceptedBid) " +
                        "FROM SilentAuction WHERE auctioneer = :auctioneer", SilentAuction.class)
                .setParameter("auctioneer", auctioneer).getResultList();
    }
}

package org.example.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.sql.Timestamp;
import java.util.List;

@Entity(name = "IncrementalAuction")
public class IncrementalAuction extends Auction {

    @Column(nullable = false)
    private Integer timeInterval; //in Seconds
    @Column(nullable = false)
    private Double startingPrice;
    @Column(nullable = false)
    private Double raisingThreshold;

    @Transient
    final private String auctionType = "IncrementalAuction";

    public IncrementalAuction() {}

    public IncrementalAuction(int id, List<AuctionPhoto> pictures, String objectName, String description, Auctioneer auctioneer, Timestamp date, String medianColor, Integer timeInterval, Double startingPrice, Double raisingThreshold) {
        super(id, pictures, objectName, description, auctioneer, date, medianColor);
        this.timeInterval = timeInterval;
        this.startingPrice = startingPrice;
        this.raisingThreshold = raisingThreshold;
    }

    public IncrementalAuction(int id, List<AuctionPhoto> pictures, String objectName, String description, Timestamp date, Auctioneer auctioneer, String medianColor, Integer timeInterval, Double startingPrice, Double raisingThreshold) {
        super(id, pictures, objectName, description, date, auctioneer, medianColor);
        this.timeInterval = timeInterval;
        this.startingPrice = startingPrice;
        this.raisingThreshold = raisingThreshold;
    }

    public IncrementalAuction(int id, String objectName, String description, Auctioneer auctioneer, Timestamp date, String medianColor, Integer timeInterval, Double startingPrice, Double raisingThreshold) {
        super(id, objectName, description, auctioneer, date, medianColor);
        this.timeInterval = timeInterval;
        this.startingPrice = startingPrice;
        this.raisingThreshold = raisingThreshold;
    }

    public IncrementalAuction(int id, String objectName, String description, Timestamp date, Auctioneer auctioneer, String medianColor, Integer timeInterval, Double startingPrice, Double raisingThreshold) {
        super(id, objectName, description, date, auctioneer, medianColor);
        this.timeInterval = timeInterval;
        this.startingPrice = startingPrice;
        this.raisingThreshold = raisingThreshold;
    }

    public IncrementalAuction(int id, List<AuctionPhoto> picturePath, String objectName, String description, Timestamp date, Auctioneer auctioneer, String medianColor, Integer timeInterval, Double startingPrice, Double raisingThreshold, List<Tag> tags) {
        super(id, picturePath, objectName, description, date, auctioneer, medianColor, tags);
        this.timeInterval = timeInterval;
        this.startingPrice = startingPrice;
        this.raisingThreshold = raisingThreshold;
    }

    public IncrementalAuction(IncrementalAuction other) {
        super(other);
        setTimeInterval(other.getTimeInterval());
        setStartingPrice(other.getStartingPrice());
        setRaisingThreshold(other.getRaisingThreshold());
    }

    public Integer getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Integer timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Double getRaisingThreshold() {
        return raisingThreshold;
    }

    public void setRaisingThreshold(Double raisingThreshold) {
        this.raisingThreshold = raisingThreshold;
    }

    public Double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getAuctionType() {
        return auctionType;
    }

    @Override
    public void setBids(List<Bid> bids) {
        super.setBids(bids);
        if(bids != null) {
            setLastBid(null);
        }
    }

    @Override
    public Boolean auctionOver() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Bid last = retrieveLastBid();
        Timestamp expiry;
        if(last != null) {
            expiry = new Timestamp(last.correctTime().getTime() + timeInterval*1000);
        } else {
            expiry = new Timestamp(correctDate().getTime() + timeInterval*1000);
        }
        return now.after(expiry);
    }

    @Override
    public Auction toJsonFriendly() {
        IncrementalAuction auction = new IncrementalAuction(this);
        auction.setAuctioneerUsername(this.getAuctioneer().getUsername());
        auction.setAuctioneer(null);
        auction.getBids().replaceAll(Bid::toJsonFriendly);
        auction.updateLastBid();
        auction.getPictures().replaceAll(AuctionPhoto::toJsonFriendly);
        return auction;
    }
}

package org.example.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.sql.Timestamp;

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

    public IncrementalAuction(int id, String picturePath, String objectName, String description, Auctioneer auctioneer, Timestamp date, Integer timeInterval, Double startingPrice, Double raisingThreshold) {
        super(id, picturePath, objectName, description, auctioneer, date);
        this.timeInterval = timeInterval;
        this.startingPrice = startingPrice;
        this.raisingThreshold = raisingThreshold;
    }

    public IncrementalAuction(int id, String picturePath, String objectName, String description, Timestamp date, Auctioneer auctioneer, Integer timeInterval, Double startingPrice, Double raisingThreshold) {
        super(id, picturePath, objectName, description, date, auctioneer);
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
    public Auction toJsonFriendly() {
        IncrementalAuction auction = new IncrementalAuction(this);
        auction.setAuctioneer(null);
        auction.setBids(null);
        return auction;
    }
}

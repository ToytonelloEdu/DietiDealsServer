package org.example.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.sql.Timestamp;

@Entity(name = "SilentAuction")
public class SilentAuction extends Auction{

    @Column(nullable = false)
    private Timestamp expirationDate;

    @Transient
    final private String auctionType = "SilentAuction";

    public SilentAuction() {}

    public SilentAuction(int id, String picturePath, String objectName, String description, Auctioneer auctioneer, Timestamp date, Timestamp expirationDate) {
        super(id, picturePath, objectName, description, auctioneer, date);
        this.expirationDate = expirationDate;
    }

    public SilentAuction(int id, String picturePath, String objectName, String description, Timestamp date, Auctioneer auctioneer, Timestamp expirationDate) {
        super(id, picturePath, objectName, description, date, auctioneer);
        this.expirationDate = expirationDate;
    }

    public SilentAuction(SilentAuction other) {
        super(other);
        setExpirationDate(other.getExpirationDate());
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getAuctionType() {
        return auctionType;
    }

    @Override
    public Auction toJsonFriendly() {
        SilentAuction auction = new SilentAuction(this);
        auction.setAuctioneer(null);
        auction.getBids().replaceAll(Bid::toJsonFriendly);
        return auction;
    }
}

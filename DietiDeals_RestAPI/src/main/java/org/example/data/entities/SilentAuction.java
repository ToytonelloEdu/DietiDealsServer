package org.example.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;

import java.sql.Timestamp;
import java.util.List;

@Entity(name = "SilentAuction")
public class SilentAuction extends Auction{

    @Column(nullable = false)
    private Timestamp expirationDate;

    @OneToOne
    private Bid acceptedBid;

    @Transient
    final private String auctionType = "SilentAuction";

    public SilentAuction() {}

    public SilentAuction(int id, List<AuctionPhoto> pictures, String objectName, String description, Auctioneer auctioneer, Timestamp date, String medianColor, Timestamp expirationDate) {
        super(id, pictures, objectName, description, auctioneer, date, medianColor);
        this.expirationDate = expirationDate;
    }

    public SilentAuction(int id, List<AuctionPhoto> picturePath, String objectName, String description, Timestamp date, Auctioneer auctioneer, String medianColor, Timestamp expirationDate) {
        super(id, picturePath, objectName, description, date, auctioneer, medianColor);
        this.expirationDate = expirationDate;
    }

    public SilentAuction(int id, String objectName, String description, Auctioneer auctioneer, Timestamp date, String medianColor, Timestamp expirationDate) {
        super(id, objectName, description, auctioneer, date, medianColor);
        this.expirationDate = expirationDate;
    }

    public SilentAuction(int id, String objectName, String description, Timestamp date, Auctioneer auctioneer, String medianColor, Timestamp expirationDate) {
        super(id, objectName, description, date, auctioneer, medianColor);
        this.expirationDate = expirationDate;
    }

    public SilentAuction(int id, List<AuctionPhoto> picturePath, String objectName, String description, Timestamp date, Auctioneer auctioneer, String medianColor, Timestamp expirationDate, List<Tag> tags) {
        super(id, picturePath, objectName, description, date, auctioneer, medianColor, tags);
        this.expirationDate = expirationDate;
    }

    public SilentAuction(SilentAuction other) {
        super(other);
        setExpirationDate(other.expirationDate);
        setAcceptedBid(other.getAcceptedBid());
    }

    public Timestamp getExpirationDate() {
        return new Timestamp(expirationDate.getTime() - 2*60*60*1000);
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Bid getAcceptedBid() {
        return acceptedBid;
    }

    public void setAcceptedBid(Bid acceptedBid) {
        this.acceptedBid = acceptedBid;
    }

    public String getAuctionType() {
        return auctionType;
    }

    @Override
    public Boolean auctionOver() {
        if (acceptedBid != null) return true;

        return (new Timestamp(System.currentTimeMillis())).after(getExpirationDate());
    }

    @Override
    public Auction toJsonFriendly() {
        SilentAuction auction = new SilentAuction(this);
        auction.setAuctioneerUsername(this.getAuctioneer().getUsername());
        auction.setAuctioneer(null);
        auction.getBids().replaceAll(Bid::toJsonFriendly);
        acceptedBid = acceptedBid.toJsonFriendly();
        auction.getPictures().replaceAll(AuctionPhoto::toJsonFriendly);
        return auction;
    }
}

package org.example.data.entities;

import jakarta.persistence.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class AuctionPhoto {
    @Id @GeneratedValue
    private int id;

    private String path;

    @ManyToOne(fetch = LAZY, optional = false, cascade = CascadeType.MERGE)
    private Auction auction;

    public AuctionPhoto() {}

    @SuppressWarnings("unused")
    public AuctionPhoto(int id, String path, Auction auction) {
        this.id = id;
        this.path = path;
        this.auction = auction.toJsonFriendly();
    }

    public AuctionPhoto(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public AuctionPhoto(String path, Auction auction) {
        this.path = path;
        this.auction = auction.toJsonFriendly();
    }

    protected AuctionPhoto(AuctionPhoto other) {
        this.id = other.id;
        this.path = other.path;
        this.auction = other.auction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public AuctionPhoto toJsonFriendly() {
        AuctionPhoto auctionPhoto = new AuctionPhoto(this);
        auctionPhoto.setAuction(null);
        return auctionPhoto;
    }
}

package org.example.data.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;

import static jakarta.persistence.FetchType.LAZY;

@Entity(name = "Bid")
public class Bid {

    @Id @GeneratedValue
    private int id;

    @ManyToOne(fetch = LAZY, optional = false, cascade = CascadeType.ALL)
    private Auction auction;

    @ManyToOne(fetch = LAZY, optional = false, cascade = CascadeType.ALL)
    private Buyer buyer;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private Timestamp time;

    @Transient
    private String bidder; //buyer.username

    public Bid() {}
    public Bid(int id, Buyer buyer, double amount, Timestamp time) {
        this.id = id;
        this.bidder = buyer.getUsername();
        this.amount = amount;
        this.time = time;
    }

    public Bid(int id, Auction auction, double amount, Timestamp time) {
        this.id = id;
        System.out.println(auction.toString());
        this.auction = auction;
        this.amount = amount;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Auction getAuction() {
        if(auction != null){
            return auction.toJsonFriendly();
        }
        return null;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getBidder() {
        return bidder;
    }

    public void setBidder(String bidder) {
        this.bidder = bidder;
    }

}

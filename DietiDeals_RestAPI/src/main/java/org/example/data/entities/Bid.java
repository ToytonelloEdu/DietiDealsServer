package org.example.data.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Comparator;

@Entity(name = "Bid")
public class Bid {

    @Id @GeneratedValue
    private int id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Auction auction;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Buyer buyer;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private Timestamp time;

    @Transient
    private String bidder; //buyer.username

    public Bid() {}

    public Bid(int id, Auction auction, Buyer buyer, double amount, Timestamp time) {
        this.id = id;
        this.auction = auction;
        this.buyer = buyer;
        this.amount = amount;
        this.time = time;
    }

    public Bid(int id, Buyer buyer, double amount, Timestamp time) {
        this.id = id;
        this.bidder = buyer.getUsername();
        this.amount = amount;
        this.time = time;
    }

    public Bid(int id, Auction auction, double amount, Timestamp time) {
        this.id = id;
        this.auction = auction;
        this.auction.getPictures().replaceAll(AuctionPhoto::toJsonFriendly);
        this.amount = amount;
        this.time = time;
    }

    protected Bid(Bid other) {
        setId(other.getId());
        setAuction(other.auction);
        setBuyer(other.getBuyer());
        setAmount(other.getAmount());
        setTime(other.getTime());
        if (other.getBidder() != null)
            setBidder(other.getBidder());
        else
            setBidder(other.getBuyer().getUsername());
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

    public int auctionId(){
        return auction.getId();
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

    public Bid toJsonFriendly() {
        Bid newBid = new Bid(this);
        newBid.buyer = null;
        newBid.auction = null;
        return newBid;
    }

    public static Comparator<Bid> compareByTimeDesc = (b1, b2) -> b2.time.compareTo(b1.time);
}

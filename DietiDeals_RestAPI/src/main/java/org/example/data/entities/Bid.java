package org.example.data.entities;

import jakarta.persistence.*;

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

    @Transient
    private String bidder; //buyer.username

    public Bid() {}
    public Bid(int id, Buyer buyer, double amount) {
        this.id = id;
        this.bidder = buyer.getUsername();
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected Auction getAuction() {
        return auction;
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

    public String getBidder() {
        return bidder;
    }

    public void setBidder(String bidder) {
        this.bidder = bidder;
    }

}

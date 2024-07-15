package org.example.data.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Entity(name = "Auction")
public class Auction {

    @Id @GeneratedValue
    private int id;

    private String picturePath;
    private String objectName;
    private String description;

    @ManyToOne(fetch = LAZY)
    private Auctioner auctioner;

    private Timestamp date;

    @ManyToMany
    @JoinTable(name = "auctionTags")
    private Set<Tag> tags;

    @OneToMany(mappedBy= "auction")
    private Set<Bid> bids;

    public Auction() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Auctioner getAuctioner() {
        return auctioner;
    }

    public void setAuctioner(Auctioner auctioner) {
        this.auctioner = auctioner;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }
}

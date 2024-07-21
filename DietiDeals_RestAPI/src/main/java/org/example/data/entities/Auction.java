package org.example.data.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity(name = "Auction")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract public class Auction {
    @Id @GeneratedValue
    private int id;

    private String picturePath;
    @Column(nullable = false)
    private String objectName;
    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = LAZY, optional = false)
    private Auctioneer auctioneer;
    @Column(nullable = false)
    private Timestamp date;

    @ManyToMany
    @JoinTable(name = "auctionTags")
    private List<Tag> tags;

    @OneToMany(mappedBy= "auction", fetch = LAZY)
    private List<Bid> bids;

    @Transient
    private Bid lastBid;
    @Transient
    private String auctioneerUsername;

    public Auction() {}
    public Auction(int id, String picturePath, String objectName, String description, Timestamp date) {
        this.id = id;
        this.picturePath = picturePath;
        this.objectName = objectName;
        this.description = description;
        this.date = date;
    }
    public Auction(int id, String picturePath, String objectName, String description, Auctioneer auctioneer, Timestamp date) {
        this(id, picturePath, objectName, description, date);
        this.auctioneerUsername = auctioneer.getUsername();
    }

    public Auction(int id, String picturePath, String objectName, String description, Timestamp date, Auctioneer auctioneer) {
        this(id, picturePath, objectName, description, date);
        this.auctioneer = auctioneer;
    }

    protected Auction(Auction other) {
        setId(other.getId());
        setPicturePath(other.getPicturePath());
        setObjectName(other.getObjectName());
        setDescription(other.getDescription());
        setAuctioneer(other.getAuctioneer());
        setAuctioneerUsername(other.getAuctioneerUsername());
        setDate(other.getDate());
        setTags(other.getTags());
        setBids(other.getBids());
        setLastBid(other.getLastBid());
    }

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

    public Auctioneer getAuctioneer() {
        return auctioneer;
    }

    public void setAuctioneer(Auctioneer auctioneer) {
        this.auctioneer = auctioneer;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public String getAuctioneerUsername() {
        return auctioneerUsername;
    }

    public void setAuctioneerUsername(String auctioneerUsername) {
        this.auctioneerUsername = auctioneerUsername;
    }

    public Bid getLastBid() {
        return lastBid;
    }

    public void setLastBid(Bid lastBid) {
        this.lastBid = lastBid;
    }

    abstract public String getAuctionType();

    public void updateLastBid(){
        if (bids != null && !bids.isEmpty()) {
            int size = bids.size();
            setLastBid(bids.get(size - 1));
        }
    }

    public Bid retrieveLastBid() {
        if (bids != null && !bids.isEmpty()) {
            int size = bids.size();
            return bids.get(size - 1);
        }
        return null;
    }

    public void setLastBid_fromList(List<Bid> bids) {
        if (bids != null && !bids.isEmpty()) {
            int size = bids.size();
            setLastBid(bids.get(size - 1));
        }


    }

    public boolean addBid(Bid bid) {
        if(bids == null) {
            bids = new ArrayList<>();
        }
        if(!bids.contains(bid) && bid.getAmount() > lastBid.getAmount())
        {
            boolean res = bids.add(bid);
            lastBid = retrieveLastBid();
            return true;
        } else return false;
    }

    abstract public Auction toJsonFriendly();

    public static final Comparator<Auction> ComparatorByDate = new Comparator<Auction>() {
        public int compare(Auction a1, Auction a2) {
            return a2.getDate().compareTo(a1.getDate());
        }
    };
}

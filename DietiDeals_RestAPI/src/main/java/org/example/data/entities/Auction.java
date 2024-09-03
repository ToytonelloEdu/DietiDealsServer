package org.example.data.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@Entity(name = "Auction")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract public class Auction {
    @Id @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String objectName;
    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = LAZY, optional = false)
    private Auctioneer auctioneer;
    @Column(nullable = false)
    private Timestamp date;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "auctionTags",
            joinColumns = {@JoinColumn(name = "auction_id")},
            inverseJoinColumns = {@JoinColumn(name = "tags_tagname")}
    )
    private List<Tag> tags;

    @OneToMany(mappedBy= "auction", fetch = EAGER)
    private List<Bid> bids;

    @OneToMany(mappedBy = "auction", fetch = LAZY)
    private List<AuctionPhoto> pictures;
    private String medianColor;

    @Column(nullable = false)
    private Boolean notified = false;

    @Transient
    private Bid lastBid;
    @Transient
    private String auctioneerUsername;

    public Auction() {}
    public Auction(int id, List<AuctionPhoto> pictures, String objectName, String description, Timestamp date, String medianColor) {
        this.id = id;
        this.pictures = pictures;
        this.objectName = objectName;
        this.description = description;
        this.date = date;
        this.medianColor = medianColor;
    }
    public Auction(int id, List<AuctionPhoto> pictures, String objectName, String description, Auctioneer auctioneer, Timestamp date, String medianColor) {
        this(id, pictures, objectName, description, date, medianColor);
        this.auctioneerUsername = auctioneer.getUsername();
    }

    public Auction(int id, List<AuctionPhoto> pictures, String objectName, String description, Timestamp date, Auctioneer auctioneer, String medianColor) {
        this(id, pictures, objectName, description, date, medianColor);
        this.auctioneer = auctioneer;
    }

    public Auction(int id, List<AuctionPhoto> pictures, String objectName, String description, Timestamp date, Auctioneer auctioneer, String medianColor, List<Tag> tags) {
        this(id, pictures, objectName, description, date, medianColor);
        this.auctioneer = auctioneer;
        this.tags = tags;
    }

    public Auction(int id, String objectName, String description, Timestamp date, String medianColor) {
        this.id = id;
        this.objectName = objectName;
        this.description = description;
        this.date = date;
        this.medianColor = medianColor;
    }
    public Auction(int id, String objectName, String description, Auctioneer auctioneer, Timestamp date, String medianColor) {
        this(id, objectName, description, date, medianColor);
        this.auctioneerUsername = auctioneer.getUsername();
    }

    public Auction(int id, String objectName, String description, Timestamp date, Auctioneer auctioneer, String medianColor) {
        this(id, objectName, description, date, medianColor);
        this.auctioneer = auctioneer;
    }

    protected Auction(Auction other) {
        setId(other.getId());
        setPictures(other.getPictures());
        setObjectName(other.getObjectName());
        setDescription(other.getDescription());
        setAuctioneer(other.getAuctioneer());
        setAuctioneerUsername(other.getAuctioneerUsername());
        setDate(other.date);
        setTags(other.getTags());
        setBids(other.getBids());
        setLastBid(other.getLastBid());
        setMedianColor(other.getMedianColor());
        notified(other.notified());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<AuctionPhoto> getPictures() {
        return pictures;
    }

    public void setPictures(List<AuctionPhoto> picturePath) {
        this.pictures = picturePath;
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

    public Timestamp correctDate() {
        return new Timestamp(date.getTime() - 2*60*60*1000);
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

    public String getMedianColor() {
        return medianColor;
    }

    public void setMedianColor(String medianColor) {
        this.medianColor = medianColor;
    }

    public Boolean notified() {
        return notified;
    }

    public void notified(Boolean notified) {
        this.notified = notified;
    }

    abstract public String getAuctionType();

    abstract public Boolean auctionOver();

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

    public AuctionPhoto firstPhoto() {
        if(pictures != null && !pictures.isEmpty())
            return pictures.get(0);

        return null;
    }

    public Auction toHomeJsonFriendly() {
        Auction auction = this.toJsonFriendly();
        auction.bids = null;
        return auction;
    }

    abstract public Auction toJsonFriendly();

    public static final Comparator<Auction> ComparatorByDate = (a1, a2) -> a2.getDate().compareTo(a1.getDate());


}

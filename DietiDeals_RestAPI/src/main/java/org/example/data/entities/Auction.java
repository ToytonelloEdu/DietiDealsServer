package org.example.data.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Set;

@Entity(name = "Auction")
public class Auction {

    @Id @GeneratedValue
    private int id;

    private String picturePath;
    private String object;
    private String description;

    @ManyToOne
    private Auctioner auctioner;
    private Timestamp date;

    @ManyToMany
    @JoinTable(name = "auctionTags")
    private Set<Tag> tags;

    @OneToMany(mappedBy= "auction")
    private Set<Bid> bids;
}

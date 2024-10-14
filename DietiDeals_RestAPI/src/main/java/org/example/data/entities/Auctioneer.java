package org.example.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

import java.sql.Timestamp;
import java.util.List;

@Entity(name = "Auctioneer")
public class Auctioneer extends User{

    @OneToMany
    private List<Auction> auctions;


    @Transient
    private final String userType = "Auctioneer";

    public List<Auction> getAuctions() {
        return auctions;
    }

    public void setAuctions(List<Auction> auctions) {
        this.auctions = auctions;
    }

    @Override
    public String getUserType() {
        return userType;
    }

    public Auctioneer(){}

    public Auctioneer(User user) {
        copyAllFields(user);
    }

    public Auctioneer(Auctioneer other) {
        copyAllFields(other);
        auctions = other.auctions;
    }


    public Auctioneer(
            String username,
            String userType,
            String email,
            String password,
            String firstName,
            String lastName,
            String proPicPath,
            String bio,
            String nationality,
            String gender,
            Timestamp birthdate,
            Links links
    ) {
        super(username, userType, email, password, firstName, lastName, proPicPath, bio, nationality, gender, birthdate, links);
    }



    @Override
    public User toJsonFriendly() {
        Auctioneer other = new Auctioneer(this);
        other.setAuctions(null);
        return other;
    }
}

package org.example.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

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
        setUsername(user.getUsername());
        setUserType(user.getUserType());
        setPassword(user.getPassword());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setBio(user.getBio());
        setNationality(user.getNationality());
        setProPicPath(user.getProPicPath());
    }

    public Auctioneer(String username, String userType, String email, String password, String firstName, String lastName, String proPicPath, String bio, String nationality) {
        super(username, userType, email, password, firstName, lastName, proPicPath, bio, nationality);
    }
}

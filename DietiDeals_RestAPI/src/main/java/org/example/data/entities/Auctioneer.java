package org.example.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity(name = "Auctioneer")
public class Auctioneer extends User{

    @OneToMany
    private List<Auction> auctions;

    public List<Auction> getAuctions() {
        return auctions;
    }

    public void setAuctions(List<Auction> auctions) {
        this.auctions = auctions;
    }

    public Auctioneer(){}
    public Auctioneer(User user) {
        setUsername(user.getUsername());
        setPassword(user.getPassword());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setBio(user.getBio());
        setNationality(user.getNationality());
    }
}

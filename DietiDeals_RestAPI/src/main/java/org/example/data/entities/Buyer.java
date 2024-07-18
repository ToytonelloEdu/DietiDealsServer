package org.example.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity(name = "Buyer")
public class Buyer extends User{
    @OneToMany(mappedBy= "buyer")
    private List<Bid> bids;

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public Buyer(){}
    public Buyer(User user) {
        setUsername(user.getUsername());
        setPassword(user.getPassword());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setBio(user.getBio());
        setNationality(user.getNationality());
    }
    public Buyer(String username, String userType, String email, String password, String firstName, String lastName, String proPicPath, String bio, String nationality) {
        super(username, userType, email, password, firstName, lastName, proPicPath, bio, nationality);
    }
}

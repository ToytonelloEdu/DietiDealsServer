package org.example.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

import java.sql.Timestamp;
import java.util.List;

@Entity(name = "Buyer")
public class Buyer extends User{
    @OneToMany(mappedBy= "buyer")
    private List<Bid> bids;

    @Transient
    private final String userType = "Buyer";

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    @Override
    public String getUserType() {
        return userType;
    }

    public Buyer(){}
    public Buyer(User user) {
        setUsername(user.getUsername());
        setUserType(user.getUserType());
        setPassword(user.getPassword());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setBio(user.getBio());
        setNationality(user.getNationality());
        setProPicPath(user.getProPicPath());
        setBirthdate(user.getBirthdate());
        setGender(user.getGender());
        setLinks(user.getLinks());
    }
    public Buyer(
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

    public Buyer(Buyer other) {
        setUsername(other.getUsername());
        setUserType(other.getUserType());
        setPassword(other.getPassword());
        setEmail(other.getEmail());
        setFirstName(other.getFirstName());
        setLastName(other.getLastName());
        setBio(other.getBio());
        setNationality(other.getNationality());
        setProPicPath(other.getProPicPath());
        setBirthdate(other.getBirthdate());
        setGender(other.getGender());
        setLinks(other.getLinks());
        bids = other.bids;
    }

    @Override
    public Buyer toJsonFriendly() {
        Buyer other = new Buyer(this);
        other.setBids(null);
        return other;
    }
}

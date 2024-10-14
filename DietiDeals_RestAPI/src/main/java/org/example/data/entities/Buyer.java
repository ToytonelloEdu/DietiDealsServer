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
        copyAllFields(user);
    }
    public Buyer(Buyer other) {
        copyAllFields(other);
        bids = other.bids;
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


    @Override
    public Buyer toJsonFriendly() {
        Buyer other = new Buyer(this);
        other.setBids(null);
        return other;
    }
}

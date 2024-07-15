package org.example.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity(name = "Buyer")
public class Buyer extends User{
    @OneToMany(mappedBy= "buyer")
    private Set<Bid> bids;
}

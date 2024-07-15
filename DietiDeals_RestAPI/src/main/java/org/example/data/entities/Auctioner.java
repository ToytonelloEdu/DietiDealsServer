package org.example.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity(name = "Auctioner")
public class Auctioner extends User{

    @OneToMany
    private List<Auction> auctions;
}

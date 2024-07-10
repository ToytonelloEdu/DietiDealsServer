package org.example.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import static jakarta.persistence.FetchType.LAZY;

@Entity(name = "Bid")
public class Bid {

    @Id @GeneratedValue
    private int id;

    @ManyToOne(fetch = LAZY)
    private Auction auction;

    @ManyToOne
    private Buyer buyer;
}

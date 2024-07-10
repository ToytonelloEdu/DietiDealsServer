package org.example.data.repos;

import org.example.data.entities.Auction;

import java.util.List;


public interface AuctionsRepository {

    List<Auction> getAuctions();

    Auction getAuctionByID(int id);

    Auction addAuction(Auction auction);

    Auction deleteAuction(int id);


}


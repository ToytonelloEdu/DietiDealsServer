package org.example.data.repos;

import org.example.data.entities.Auction;
import org.example.data.entities.Auctioneer;

import java.util.List;


public interface AuctionsRepository {

    List<Auction> getAuctions();

    Auction getAuctionByID(int id);

    List<Auction> getAuctionsByAuctioneer(Auctioneer auctioneer);

    Auction addAuction(Auction auction);

    Auction deleteAuction(int id);


}


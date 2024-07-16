package org.example.data.repos;

import org.example.data.entities.Auction;
import org.example.data.entities.Bid;
import org.example.data.entities.Buyer;

import java.util.List;

public interface BidsRepository {

    List<Bid> getBidsByAuction(Auction auction);

    List<Bid> getBidsByUser(Buyer buyer);

}

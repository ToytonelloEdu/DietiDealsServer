package org.example.data.repos;

import org.example.data.entities.Auction;
import org.example.data.entities.Auctioneer;

import java.util.List;


public interface AuctionsRepository {

    List<Auction> getAuctions();

    List<Auction> getAuctionsQueried(Query query);

    Auction getAuctionByID(int id);

    List<Auction> getAuctionsByAuctioneer(Auctioneer auctioneer);

    List<Auction> getAuctionsNotNotified();

    Auction addAuction(Auction auction);

    Auction updateAuction(Auction auction);

    Auction deleteAuction(int id);

    class Query {
        final String object;
        final String vendor;
        final List<String> tags;

        public Query(String vendor, String object, List<String> tags) {
            this.vendor = vendor;
            this.object = object;
            this.tags = tags;
        }
    }

}


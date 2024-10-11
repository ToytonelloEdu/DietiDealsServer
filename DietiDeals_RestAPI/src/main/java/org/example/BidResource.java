package org.example;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.entities.Bid;
import org.example.data.entities.Buyer;
import org.example.data.repos.BidsDbRepository;
import org.example.data.repos.BidsRepository;
import org.example.filter.RequireAuth;

import java.sql.Timestamp;

@Path("bids")
public class BidResource {
    private static BidsRepository bidsRepo;

    @SuppressWarnings("unused")
    public BidResource() {
        bidsRepo = BidsDbRepository.getInstance();
    }

    @SuppressWarnings("unused")
    public BidResource(BidsRepository bidsRepo) {
        BidResource.bidsRepo = bidsRepo;
    }

    @POST
    @RequireAuth
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBid(InputBid bid) {
        try {
            Bid resBid= bidsRepo.addBid(bid.toBid());
            return Response.status(Response.Status.CREATED).entity(resBid.toJsonFriendly()).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }


    public static class InputBid {
        private int id;
        private AuctionResource.InputAuction auction;
        private Buyer buyer;
        private String bidder;
        private double amount;
        private Timestamp time;

        public InputBid(){}

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public AuctionResource.InputAuction getAuction() {
            return auction;
        }

        public void setAuction(AuctionResource.InputAuction auction) {
            this.auction = auction;
        }

        public Buyer getBuyer() {
            return buyer;
        }

        public void setBuyer(Buyer buyer) {
            this.buyer = buyer;
        }

        @SuppressWarnings("unused")
        public String getBidder() {
            return bidder;
        }

        @SuppressWarnings("unused")
        public void setBidder(String bidder) {
            this.bidder = bidder;
        }

        @SuppressWarnings("unused")
        public Timestamp getTime() {
            return time;
        }

        @SuppressWarnings("unused")
        public void setTime(Timestamp time) {
            this.time = time;
        }

        @SuppressWarnings("unused")
        public double getAmount() {
            return amount;
        }

        @SuppressWarnings("unused")
        public void setAmount(double amount) {
            this.amount = amount;
        }

        public Bid toBid() {
            return new Bid(
                    id,
                    auction.toAuction(),
                    buyer,
                    amount,
                    time
            );
        }
    }
}

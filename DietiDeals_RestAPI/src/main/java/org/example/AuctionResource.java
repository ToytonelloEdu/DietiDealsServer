package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.entities.*;
import org.example.data.repos.AuctionsDbRepository;
import org.example.data.repos.AuctionsRepository;

import java.sql.Timestamp;
import java.util.List;

@Path("auctions")
public class AuctionResource {
    final AuctionsRepository auctionsRepo;

    public AuctionResource(){
        auctionsRepo = AuctionsDbRepository.getInstance();
    }

    public AuctionResource(AuctionsRepository auctionsRepo){
        this.auctionsRepo = auctionsRepo;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Auction> getAuction() {
        return auctionsRepo.getAuctions();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Auction getAuctionById(@PathParam("id") int id) {
        return auctionsRepo.getAuctionByID(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAuction(InputAuction auction) {
        try{
            Auction resAuction = auctionsRepo.addAuction(auction.toAuction());
            return Response.status(Response.Status.CREATED).entity(resAuction).build();
        } catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteAuction(@PathParam("id") int id) {
        try{
            Auction resAuction = auctionsRepo.deleteAuction(id);
            return Response.status(Response.Status.OK).entity(resAuction).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    public static class InputAuction{
        private int id;
        private String auctionType;
        private String objectName;
        private String description;
        private Auctioneer auctioneer;
        private Timestamp date;
        private List<Tag> tags;
        private List<Bid> bids;
        private Bid lastBid;
        private String auctioneerUsername;
        private Timestamp expirationDate;
        private Integer timeInterval;
        private Double startingPrice;
        private Double raisingThreshold;
        private String picturePath;

        public InputAuction() {}

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAuctionType() {
            return auctionType;
        }

        public void setAuctionType(String auctionType) {
            this.auctionType = auctionType;
        }

        public String getObjectName() {
            return objectName;
        }

        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Auctioneer getAuctioneer() {
            return auctioneer;
        }

        public void setAuctioneer(Auctioneer auctioneer) {
            this.auctioneer = auctioneer;
        }

        public Timestamp getDate() {
            return date;
        }

        public void setDate(Timestamp date) {
            this.date = date;
        }

        public List<Tag> getTags() {
            return tags;
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

        public List<Bid> getBids() {
            return bids;
        }

        public void setBids(List<Bid> bids) {
            this.bids = bids;
        }

        public Bid getLastBid() {
            return lastBid;
        }

        public void setLastBid(Bid lastBid) {
            this.lastBid = lastBid;
        }

        public String getAuctioneerUsername() {
            return auctioneerUsername;
        }

        public void setAuctioneerUsername(String auctioneerUsername) {
            this.auctioneerUsername = auctioneerUsername;
        }

        public Timestamp getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(Timestamp expirationDate) {
            this.expirationDate = expirationDate;
        }

        public Integer getTimeInterval() {
            return timeInterval;
        }

        public void setTimeInterval(Integer timeInterval) {
            this.timeInterval = timeInterval;
        }

        public Double getStartingPrice() {
            return startingPrice;
        }

        public void setStartingPrice(Double startingPrice) {
            this.startingPrice = startingPrice;
        }

        public Double getRaisingThreshold() {
            return raisingThreshold;
        }

        public void setRaisingThreshold(Double raisingThreshold) {
            this.raisingThreshold = raisingThreshold;
        }

        public String getPicturePath() {
            return picturePath;
        }

        public void setPicturePath(String picturePath) {
            this.picturePath = picturePath;
        }

        public Auction toAuction() {
            if(auctionType.equals("IncrementalAuction")) {
                return new IncrementalAuction(
                        id,
                        picturePath,
                        objectName,
                        description,
                        date,
                        auctioneer,
                        timeInterval,
                        startingPrice,
                        raisingThreshold

                );
            } else if (auctionType.equals("SilentAuction")) {
                return new SilentAuction(
                        id,
                        picturePath,
                        objectName,
                        description,
                        date,
                        auctioneer,
                        expirationDate

                );
            }
            return null;
        }
    }
}

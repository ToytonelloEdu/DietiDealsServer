package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.entities.*;
import org.example.data.repos.AuctionsDbRepository;
import org.example.data.repos.AuctionsRepository;
import org.example.filter.RequireAuth;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Path("auctions")
public class AuctionResource {
    final AuctionsRepository auctionsRepo;

    @SuppressWarnings("unused")
    public AuctionResource(){
        auctionsRepo = AuctionsDbRepository.getInstance();
    }

    @SuppressWarnings("unused")
    public AuctionResource(AuctionsRepository auctionsRepo){
        this.auctionsRepo = auctionsRepo;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Auction> getAuctionsQueried(
            @QueryParam("vendor") String vendor,
            @QueryParam("object") String object,
            @QueryParam("tag1") String tag1,
            @QueryParam("tag2") String tag2,
            @QueryParam("tag3") String tag3
    ) {
        if (vendor == null && object == null && tag1 == null && tag2 == null && tag3 == null) {
            return auctionsRepo.getAuctions();
        } else {
            List<String> tags = getTagsList(tag1, tag2, tag3);
            return auctionsRepo.getAuctionsQueried(
                    new AuctionsRepository.Query(vendor, object, tags)
            );
        }
    }

    private static List<String> getTagsList(String tag1, String tag2, String tag3) {
        List<String> tags = new ArrayList<>();
        if(tag1 != null) tags.add(tag1);
        if(tag2 != null) tags.add(tag2);
        if(tag3 != null) tags.add(tag3);
        return tags;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Auction getAuctionById(@PathParam("id") int id) {
        try{
            return auctionsRepo.getAuctionByID(id);
        } catch (NullPointerException e){
            return null;
        }
    }

    @POST
    @RequireAuth
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAuction(InputAuction auction) {
        try{
            Auction resAuction = auctionsRepo.addAuction(auction.toAuction());
            return Response.status(Response.Status.CREATED).entity(resAuction).build();
        } catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @RequireAuth
    @Path("{id}")
    public Response deleteAuction(@PathParam("id") int id) {
        try{
            Auction resAuction = auctionsRepo.deleteAuction(id);
            return Response.status(Response.Status.OK).entity(resAuction).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @PUT
    @RequireAuth
    @Path("{id}/accept/{bidId}")
    public Response acceptBid(@HeaderParam("Authorization") @PathParam("id") int id, @PathParam("bidId") int bid) {
        try{
            Auction auction = auctionsRepo.acceptBid(id, bid).toJsonFriendly();
            return Response.status(Response.Status.OK).entity(auction).build();
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }catch (Exception e){
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
        private String medianColor;
        private List<Tag> tags;
        private List<Bid> bids;
        private Bid lastBid;
        private String auctioneerUsername;
        private Timestamp expirationDate;
        private BidResource.InputBid acceptedBid;
        private Integer timeInterval;
        private Double startingPrice;
        private Double raisingThreshold;
        private List<AuctionPhoto> pictures;

        public InputAuction() {}

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @SuppressWarnings("unused")
        public String getAuctionType() {
            return auctionType;
        }

        @SuppressWarnings("unused")
        public void setAuctionType(String auctionType) {
            this.auctionType = auctionType;
        }

        @SuppressWarnings("unused")
        public String getObjectName() {
            return objectName;
        }

        @SuppressWarnings("unused")
        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        @SuppressWarnings("unused")
        public String getDescription() {
            return description;
        }

        @SuppressWarnings("unused")
        public void setDescription(String description) {
            this.description = description;
        }

        public Auctioneer getAuctioneer() {
            return auctioneer;
        }

        public void setAuctioneer(Auctioneer auctioneer) {
            this.auctioneer = auctioneer;
        }

        @SuppressWarnings("unused")
        public Timestamp getDate() {
            return date;
        }

        @SuppressWarnings("unused")
        public void setDate(Timestamp date) {
            this.date = date;
        }

        @SuppressWarnings("unused")
        public String getMedianColor() {
            return medianColor;
        }

        @SuppressWarnings("unused")
        public void setMedianColor(String medianColor) {
            this.medianColor = medianColor;
        }

        @SuppressWarnings("unused")
        public List<Tag> getTags() {
            return tags;
        }

        @SuppressWarnings("unused")
        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

        @SuppressWarnings("unused")
        public List<Bid> getBids() {
            return bids;
        }

        @SuppressWarnings("unused")
        public void setBids(List<Bid> bids) {
            this.bids = bids;
        }

        @SuppressWarnings("unused")
        public Bid getLastBid() {
            return lastBid;
        }

        @SuppressWarnings("unused")
        public void setLastBid(Bid lastBid) {
            this.lastBid = lastBid;
        }

        @SuppressWarnings("unused")
        public String getAuctioneerUsername() {
            return auctioneerUsername;
        }

        @SuppressWarnings("unused")
        public void setAuctioneerUsername(String auctioneerUsername) {
            this.auctioneerUsername = auctioneerUsername;
        }

        @SuppressWarnings("unused")
        public Timestamp getExpirationDate() {
            return expirationDate;
        }

        @SuppressWarnings("unused")
        public void setExpirationDate(Timestamp expirationDate) {
            this.expirationDate = expirationDate;
        }

        @SuppressWarnings("unused")
        public Integer getTimeInterval() {
            return timeInterval;
        }

        @SuppressWarnings("unused")
        public void setTimeInterval(Integer timeInterval) {
            this.timeInterval = timeInterval;
        }

        @SuppressWarnings("unused")
        public Double getStartingPrice() {
            return startingPrice;
        }

        @SuppressWarnings("unused")
        public void setStartingPrice(Double startingPrice) {
            this.startingPrice = startingPrice;
        }

        @SuppressWarnings("unused")
        public Double getRaisingThreshold() {
            return raisingThreshold;
        }

        @SuppressWarnings("unused")
        public void setRaisingThreshold(Double raisingThreshold) {
            this.raisingThreshold = raisingThreshold;
        }

        @SuppressWarnings("unused")
        public List<AuctionPhoto> getPictures() {
            return pictures;
        }

        @SuppressWarnings("unused")
        public void setPictures(List<AuctionPhoto> picturePath) {
            this.pictures = picturePath;
        }

        public Auction toAuction() {
            if(auctionType.equals("IncrementalAuction")) {
                return new IncrementalAuction(
                        id,
                        pictures,
                        objectName,
                        description,
                        date,
                        auctioneer,
                        medianColor,
                        timeInterval,
                        startingPrice,
                        raisingThreshold,
                        tags

                );
            } else if (auctionType.equals("SilentAuction")) {
                return new SilentAuction(
                        id,
                        pictures,
                        objectName,
                        description,
                        date,
                        auctioneer,
                        medianColor,
                        expirationDate,
                        tags,
                        acceptedBid.toBid()
                );
            }
            return null;
        }
    }
}

package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.entities.Auction;
import org.example.data.repos.AuctionsRepository;
import org.example.data.repos.AuctionsDbRepository;

import java.util.List;

@Path("auction")
public class AuctionResource {
    AuctionsRepository auctionsRepo;

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
    public Response addAuction(Auction auction) {
        try{
            Auction resAuction = auctionsRepo.addAuction(auction);
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
}

package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.entities.*;
import org.example.data.repos.UsersDbRepository;
import org.example.data.repos.UsersRepository;

import java.util.List;

@Path("users")
public class UserResource {
    UsersRepository usersRepo;

    public UserResource() {usersRepo = UsersDbRepository.getInstance();}

    public UserResource(final UsersRepository usersRepo) {this.usersRepo = usersRepo;}

    @GET
    @Path("{handle}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("handle") String handle) {
        return usersRepo.getUserByHandle(handle);
    }

    @PUT
    @Path("{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("username") String username) {
        //TODO
        return null;
    }

    @GET
    @Path("{handle}/auctions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Auction> getUserAuctions(@PathParam("handle") String handle) {
        User user = usersRepo.getUserByHandle(handle);
        if (user instanceof Auctioneer) {
            return ((Auctioneer) user).getAuctions();
        } else {
            throw new WebApplicationException("User is not auctioneer", Response.Status.BAD_REQUEST);
        }
    }

    @GET
    @Path("{handle}/bids")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Bid> getUserBids(@PathParam("handle") String handle) {
        User user = usersRepo.getUserByHandle(handle);
        if (user instanceof Buyer) {
            return ((Buyer) user).getBids();
        } else {
            throw new WebApplicationException("User is not buyer", Response.Status.BAD_REQUEST);
        }
    }
}

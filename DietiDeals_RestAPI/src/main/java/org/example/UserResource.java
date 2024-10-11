package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.entities.*;
import org.example.data.repos.UsersDbRepository;
import org.example.data.repos.UsersRepository;
import org.example.filter.ModifyOwnProfile;

import java.util.List;

@Path("users")
public class UserResource {
    UsersRepository usersRepo;

    @SuppressWarnings("unused")
    public UserResource() {usersRepo = UsersDbRepository.getInstance();}

    @SuppressWarnings("unused")
    public UserResource(final UsersRepository usersRepo) {this.usersRepo = usersRepo;}

    @GET
    @Path("{handle}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("handle") String handle) {
        return usersRepo.getUserByHandle(handle);
    }

    @PUT
    @Path("{username}")
    @ModifyOwnProfile
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("username") String username, SignUpResource.InputUser user) {
        if(usersRepo.getUserByUsername(username) == null) {
            System.out.println("User does not exist");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
            User updatedUser = usersRepo.updateUser(user.toUser());
        if(updatedUser == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok(updatedUser).build();
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

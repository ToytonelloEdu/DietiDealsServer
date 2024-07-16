package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.entities.User;
import org.example.data.repos.UsersDbRepository;
import org.example.data.repos.UsersRepository;

@Path("users")
public class UserResource {
    UsersRepository usersRepo;

    public UserResource() {usersRepo = UsersDbRepository.getInstance();}

    public UserResource(final UsersRepository usersRepo) {this.usersRepo = usersRepo;}

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("username") String username) {
        return usersRepo.getUserByUsername(username);
    }

    @PUT
    @Path("{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("username") String username) {
        //TODO
        return null;
    }
}

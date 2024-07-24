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
}

package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.entities.User;
import org.example.data.repos.UsersDbRepository;
import org.example.data.repos.UsersRepository;

@Path("user")
public class UserResource {
    UsersRepository usersRepo;

    public UserResource() {usersRepo = UsersDbRepository.getInstance();}

    public UserResource(final UsersRepository usersRepo) {this.usersRepo = usersRepo;}

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") int id) {
        return usersRepo.getUserByID(id);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") int id, User user) {
        //TODO
        return null;
    }
}

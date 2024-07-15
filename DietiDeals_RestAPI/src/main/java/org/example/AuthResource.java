package org.example;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.auth.AuthCredentials;
import org.example.data.repos.UsersDbRepository;
import org.example.data.repos.UsersRepository;

@Path("auth")
public class AuthResource {
    UsersRepository usersRepo;

    public AuthResource() {usersRepo = UsersDbRepository.getInstance();}

    public AuthResource(UsersRepository usersRepo) {this.usersRepo = usersRepo;}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response auth(AuthCredentials auth) {
        return null;
    }
}
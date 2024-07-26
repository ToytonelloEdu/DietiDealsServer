package org.example;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.auth.AuthController;
import org.example.auth.AuthCredentials;
import org.example.auth.JwtAuthController;
import org.example.data.entities.User;
import org.example.data.repos.UsersDbRepository;
import org.example.data.repos.UsersRepository;

import java.util.concurrent.TimeUnit;

//TODO: Separate auth for Buyer and Auctioneer
@Path("auth")
public class AuthResource {
    UsersRepository usersRepo;
    AuthController authController;

    public AuthResource() {
        usersRepo = UsersDbRepository.getInstance();
        authController = JwtAuthController.getInstance();
    }

    public AuthResource(UsersRepository usersRepo) {this.usersRepo = usersRepo;}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response auth(AuthCredentials auth) {
        if(usersRepo.verifyCredentials(auth)){
            User user = usersRepo.getUserByHandle(auth.getHandle());
            String token = authController.createToken(user.getUsername(), TimeUnit.DAYS.toMillis(1));
            return Response
                    .ok("\""+token+"\"")
                    .build();
        } else {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("\"Wrong credentials\"")
                    .build();
        }
    }


}

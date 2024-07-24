package org.example;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.auth.AuthCredentials;
import org.example.data.repos.UsersDbRepository;
import org.example.data.repos.UsersRepository;

//TODO: Separate auth for Buyer and Auctioneer
@Path("auth")
public class AuthResource {
    UsersRepository usersRepo;

    public AuthResource() {usersRepo = UsersDbRepository.getInstance();}

    public AuthResource(UsersRepository usersRepo) {this.usersRepo = usersRepo;}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response auth(AuthCredentials auth) {
        System.out.println(auth);
        if(usersRepo.verifyCredentials(auth)){
            return Response
                    .ok("\"Ok\"")
                    //JWT
                    .build();
        } else {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("\"Wrong credentials\"")
                    .build();
        }
    }


}

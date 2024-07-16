package org.example;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.entities.User;
import org.example.data.repos.UsersDbRepository;
import org.example.data.repos.UsersRepository;

@Path("signup")
public class SignUpResource {
    UsersRepository usersRepo;

    public SignUpResource() {usersRepo = UsersDbRepository.getInstance();}

    public SignUpResource(UsersRepository usersRepo) {this.usersRepo = usersRepo;}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signup(User user) {
        User resUser = usersRepo.addUser(user);
        if (resUser != null) {
            return Response.ok()
                    .entity(resUser)
                    //JWT Token
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }


    }
}

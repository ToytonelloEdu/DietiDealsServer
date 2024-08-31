package org.example;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.entities.Auctioneer;
import org.example.data.entities.Buyer;
import org.example.data.entities.User;
import org.example.data.repos.UsersDbRepository;
import org.example.data.repos.UsersRepository;

import java.sql.Timestamp;

@Path("signup")
public class SignUpResource {
    UsersRepository usersRepo;

    public SignUpResource() {usersRepo = UsersDbRepository.getInstance();}

    public SignUpResource(UsersRepository usersRepo) {this.usersRepo = usersRepo;}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signup(InputUser user) {
        User resUser = usersRepo.addUser(user.toUser());
        if (resUser != null) {
            return Response.ok()
                    .entity(resUser)
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    public static class InputUser{
        private String username;
        private String userType;
        private String email;
        private String firstName;
        private String lastName;
        private String proPicPath;
        private String bio;
        private String nationality;
        private String gender;
        private Timestamp birthdate;
        private String password;

        public InputUser() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getProPicPath() {
            return proPicPath;
        }

        public void setProPicPath(String proPicPath) {
            this.proPicPath = proPicPath;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Timestamp getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(Timestamp birthdate) {
            this.birthdate = birthdate;
        }

        public User toUser() {
            if(userType.equals("Auctioneer")) {
                return new Auctioneer(username, userType, email, password, firstName, lastName, proPicPath, bio, nationality, gender, birthdate);
            }
            else if(userType.equals("Buyer")) {
                return new Buyer(username, userType, email, password, firstName, lastName, proPicPath, bio, nationality, gender, birthdate);
            } else throw new IllegalArgumentException("Invalid user type");
        }
    }
}

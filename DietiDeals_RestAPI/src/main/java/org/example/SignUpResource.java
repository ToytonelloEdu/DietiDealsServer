package org.example;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.entities.Auctioneer;
import org.example.data.entities.Buyer;
import org.example.data.entities.Links;
import org.example.data.entities.User;
import org.example.data.repos.UsersDbRepository;
import org.example.data.repos.UsersRepository;

import java.sql.Timestamp;

@Path("signup")
public class SignUpResource {
    UsersRepository usersRepo;

    @SuppressWarnings("unused")
    public SignUpResource() {usersRepo = UsersDbRepository.getInstance();}

    @SuppressWarnings("unused")
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
        private Links links;

        public InputUser() {}

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @SuppressWarnings("unused")
        public String getUserType() {
            return userType;
        }

        @SuppressWarnings("unused")
        public void setUserType(String userType) {
            this.userType = userType;
        }

        @SuppressWarnings("unused")
        public String getEmail() {
            return email;
        }

        @SuppressWarnings("unused")
        public void setEmail(String email) {
            this.email = email;
        }

        @SuppressWarnings("unused")
        public String getFirstName() {
            return firstName;
        }

        @SuppressWarnings("unused")
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        @SuppressWarnings("unused")
        public String getLastName() {
            return lastName;
        }

        @SuppressWarnings("unused")
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @SuppressWarnings("unused")
        public String getBio() {
            return bio;
        }

        @SuppressWarnings("unused")
        public void setBio(String bio) {
            this.bio = bio;
        }

        @SuppressWarnings("unused")
        public String getNationality() {
            return nationality;
        }

        @SuppressWarnings("unused")
        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        @SuppressWarnings("unused")
        public String getPassword() {
            return password;
        }

        @SuppressWarnings("unused")
        public void setPassword(String password) {
            this.password = password;
        }

        @SuppressWarnings("unused")
        public String getProPicPath() {
            return proPicPath;
        }

        @SuppressWarnings("unused")
        public void setProPicPath(String proPicPath) {
            this.proPicPath = proPicPath;
        }

        @SuppressWarnings("unused")
        public String getGender() {
            return gender;
        }

        @SuppressWarnings("unused")
        public void setGender(String gender) {
            this.gender = gender;
        }

        @SuppressWarnings("unused")
        public Timestamp getBirthdate() {
            return birthdate;
        }

        @SuppressWarnings("unused")
        public void setBirthdate(Timestamp birthdate) {
            this.birthdate = birthdate;
        }

        @SuppressWarnings("unused")
        public Links getLinks() {
            return links;
        }

        @SuppressWarnings("unused")
        public void setLinks(Links links) {
            this.links = links;
        }

        public User toUser() {
            if(userType.equals("Auctioneer")) {
                return new Auctioneer(username, userType, email, password, firstName, lastName, proPicPath, bio, nationality, gender, birthdate, links);
            }
            else if(userType.equals("Buyer")) {
                return new Buyer(username, userType, email, password, firstName, lastName, proPicPath, bio, nationality, gender, birthdate, links);
            } else throw new IllegalArgumentException("Invalid user type");
        }
    }
}

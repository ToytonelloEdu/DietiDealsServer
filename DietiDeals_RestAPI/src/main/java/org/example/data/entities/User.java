package org.example.data.entities;

import jakarta.persistence.*;
import org.example.auth.AuthCredentials;

@Entity(name = "Users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="usertype", discriminatorType = DiscriminatorType.STRING)
abstract public class User {
    @Id @Column(length = 50)
    private String username;
    @Column(name = "usertype", insertable = false, updatable = false)
    private String userType;
    @Column(length = 50, nullable = false, unique = true)
    private String email;
    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String firstName;
    @Column(length = 20, nullable = false)
    private String lastName;

    private String proPicPath;
    @Column(length = 100, nullable = false)
    private String bio;
    @Column(nullable = false)
    private String nationality;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
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

    public String getProPicPath() {
        return proPicPath;
    }

    public void setProPicPath(String proPicPath) {
        this.proPicPath = proPicPath;
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

    protected String getPassword() {
        return /*TODO: Decrypt*/ password;
    }

    public void setPassword(String password) {
        this.password = /*TODO: Encrypt*/ password;
    }

    public User() {}
    public User(String username, String password) {
        this.username = username;
        this.setPassword(password);
    }

    public User(String username, String userType, String email, String password, String firstName, String lastName, String proPicPath, String bio, String nationality) {
        this.username = username;
        this.userType = userType;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.proPicPath = proPicPath;
        this.bio = bio;
        this.nationality = nationality;
    }

    public Boolean checkCredentials(AuthCredentials auth){
        return username.equals(auth.getUsername()) && password.equals(auth.getPassword());
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

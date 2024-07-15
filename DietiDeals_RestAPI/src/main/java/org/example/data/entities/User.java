package org.example.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "Users")
public class User {
    @Id @Column(length = 50)
    private String username;

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

    public String getPassword() {
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



    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

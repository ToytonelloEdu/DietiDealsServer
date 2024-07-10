package org.example.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.NaturalId;

@Entity(name = "User")
public class User {
    @Id @NaturalId
    private String username;
    private String email;

    private String password;

    private String firstName;
    private String lastName;

    private String bio;
    private String nationality;


    public String getPassword() {
        return /*TODO: Decrypt*/ password;
    }

    public void setPassword(String password) {
        this.password = /*TODO: Encrypt*/ password;
    }

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

package org.example.auth;

public class AuthCredentials {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthCredentials() {}
    public AuthCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthCredentials{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

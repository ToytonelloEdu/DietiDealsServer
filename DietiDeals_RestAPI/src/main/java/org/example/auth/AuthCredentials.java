package org.example.auth;

public class AuthCredentials {
    private String handle;
    private String password;

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthCredentials() {}
    public AuthCredentials(String handle, String password) {
        this.handle = handle;
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthCredentials{" +
                "handle='" + handle + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

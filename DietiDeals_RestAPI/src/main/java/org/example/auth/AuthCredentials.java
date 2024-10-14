package org.example.auth;

public class AuthCredentials {
    private String handle;
    private String password;

    public String getHandle() {
        return handle;
    }

    @SuppressWarnings("unused")
    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPassword() {
        return password;
    }

    @SuppressWarnings("unused")
    public void setPassword(String password) {
        this.password = password;
    }

    @SuppressWarnings("unused")
    public AuthCredentials() {}
    @SuppressWarnings("unused")
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

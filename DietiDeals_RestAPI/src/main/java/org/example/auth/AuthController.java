package org.example.auth;

public interface AuthController {

    String createToken(String username, long ttlMillis);
}

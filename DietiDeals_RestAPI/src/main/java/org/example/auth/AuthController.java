package org.example.auth;

public interface AuthController {

    String getUsernameClaim(String token);

    String createToken(String username, long ttlMillis);

    boolean validateToken(String token);
}

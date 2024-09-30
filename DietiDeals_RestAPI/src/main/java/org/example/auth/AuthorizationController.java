package org.example.auth;

public interface AuthorizationController {

    String getUsernameClaim(String token);

    String createToken(String username, long ttlMillis);

    boolean validateToken(String token);
}

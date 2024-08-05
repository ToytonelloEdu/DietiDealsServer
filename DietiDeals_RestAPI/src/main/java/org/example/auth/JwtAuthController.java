package org.example.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.UUID;

public class JwtAuthController implements AuthController {
    private static JwtAuthController instance;

    private JwtAuthController() {}

    public static JwtAuthController getInstance() {
        if (instance == null) {
            instance = new JwtAuthController();
        }
        return instance;
    }


    private static final String ISSUER = "todo-rest-api";
    private static final Algorithm algorithm = Algorithm.HMAC256("very_secret_key_not_to_share");
    private static final JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer(ISSUER)
            .build();

    @Override
    public String getUsernameClaim(String token) {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("username").asString();
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String createToken(String username, long ttlMillis) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ttlMillis))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }

    @Override
    public boolean validateToken(String token){
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

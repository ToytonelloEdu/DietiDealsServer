package org.example.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class JwtAuthorizationController implements AuthorizationController {
    private static JwtAuthorizationController instance;

    private JwtAuthorizationController() {}

    public static JwtAuthorizationController getInstance() {
        if (instance == null) {
            instance = new JwtAuthorizationController();
        }
        return instance;
    }


    private static final String ISSUER = "dietideals-rest-api";
    private static final Algorithm algorithm = Algorithm.HMAC256(
            Objects.requireNonNull(Dotenv.load().get("JWT_SECRET"))
    );
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
        if(username == null || username.isEmpty()){
            throw new IllegalArgumentException("Pass a valid username");
        }
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

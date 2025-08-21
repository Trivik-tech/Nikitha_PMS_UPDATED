package com.triviktech.utilities.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.triviktech.exception.token.TokenExpiredException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Service class for generating and validating JSON Web Tokens (JWTs).
 *
 * This class provides methods to create JWT tokens, extract username and role
 * from a token, and verify token validity. Tokens are signed using HMAC256
 * algorithm and include expiration and issuer information.
 */
@Service
public class JwtService {

    @Value("${security.secretKey}")
    private String secretKey;

    @Value("${security.issuer}")
    private String issuer;

    @Value("${security.expiration}")
    private long expiration; // Expiration time in milliseconds

    private Algorithm algorithm;

    private final static String USER_NAME = "username";
    private final static String USER_ROLE = "role";

    /**
     * Initializes the HMAC256 Algorithm after the bean is constructed.
     * Uses the configured secret key for signing the JWT.
     */
    @PostConstruct
    public void init() {
        algorithm = Algorithm.HMAC256(secretKey);
    }

    /**
     * Generates a JWT token for the given username and role.
     *
     * @param username the username of the user
     * @param role     the role of the user
     * @return         a signed JWT token as a String
     */
    public String generateToken(String username, String role) {
        return JWT.create()
                .withClaim(USER_NAME, username)
                .withClaim(USER_ROLE, role)
                .withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(algorithm);
    }

    /**
     * Extracts the username from the provided JWT token.
     *
     * @param token the JWT token
     * @return      the username contained in the token
     */
    public String getUsername(String token) {
        return verifyToken(token).getClaim(USER_NAME).asString();
    }

    /**
     * Extracts the role from the provided JWT token.
     *
     * @param token the JWT token
     * @return      the role contained in the token
     */
    public String getRole(String token) {
        return verifyToken(token).getClaim(USER_ROLE).asString();
    }

    /**
     * Checks if the provided JWT token is valid and not expired.
     *
     * @param token the JWT token to check
     * @return      true if the token is valid, otherwise throws exception
     * @throws TokenExpiredException if the token is invalid or expired
     */
    public boolean isTokenValid(String token) {
        try {
            DecodedJWT jwt = verifyToken(token);
            return jwt.getExpiresAt().after(new Date());
        } catch (JWTVerificationException e) {
            throw new TokenExpiredException("Invalid or expired token");
        }
    }

    /**
     * Verifies the token using the configured algorithm and issuer.
     *
     * @param token the JWT token to verify
     * @return      a DecodedJWT object if verification is successful
     * @throws JWTVerificationException if the token is invalid or signature does not match
     */
    private DecodedJWT verifyToken(String token) {
        return JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);
    }
}

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

@Service
public class JwtService {

    @Value("${security.secretKey}")
    private String secreteKey;

    @Value("${security.issuer}")
    private String issuer;

    @Value("${security.expiration}")
    private int expiration;

    private Algorithm algorithm;

    private final static String USER_NAME="username";
    private final static String USER_ROLE="role";

    @PostConstruct
    public void postConstruct(){
        algorithm=Algorithm.HMAC256(secreteKey);
    }

    public String generateToken(String username, String role){
       return JWT.create()
                .withClaim(USER_NAME,username)
                .withClaim(USER_ROLE,role)
               .withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis()+expiration))
                .sign(algorithm);
    }

    public String getUsername(String token){
        DecodedJWT verify = JWT.require(algorithm)
                .withIssuer(issuer)
                .build().verify(token);
        return verify.getClaim(USER_NAME).asString();
    }

    public String getRole(String token){
        DecodedJWT verify = JWT.require(algorithm)
               .withIssuer(issuer)
               .build().verify(token);
        return verify.getClaim(USER_ROLE).asString();
    }

    public boolean isTokenValid(String token) {
        try {
            DecodedJWT decodedJWT = verifyToken(token);
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (TokenExpiredException e) {
            throw new TokenExpiredException("Token expired please log in again");
        } catch (JWTVerificationException e) {
            return false;
        }
    }


    private DecodedJWT verifyToken(String token) {
        return JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);
    }
}

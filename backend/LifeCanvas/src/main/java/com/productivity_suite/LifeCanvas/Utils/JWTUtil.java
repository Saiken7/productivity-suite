package com.productivity_suite.LifeCanvas.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.function.Function;

@Component
public class JWTUtil {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;


    // Generation of token method
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    // Creation of JWT Token
    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() * 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Decode or Decrypt Token
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Get All
    public <T> T extractClaim (String token, Function <Claims, T> claimResolver){
        final Claims claim = extractAllClaims(token);
        return claimResolver.apply(claim);
    }

    // Get Email
    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // Get Expiration Date
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    // Get Expiration - true or false
    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // To Validate token
    public Boolean validateToken(String token, UserDetails userDetails){
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



}

package org.example.gateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Map;

public class JwtUtils {
    private static final String SECRET_KEY = "f4ds545f6a95s48a4s68d4a96w4fqs123z84g8ea4s486dg4t86j48u6g4k6l6sfg48es6hj4";
    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
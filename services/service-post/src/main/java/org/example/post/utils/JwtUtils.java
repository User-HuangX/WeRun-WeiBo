package org.example.post.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Setter
public class JwtUtils {
    //一个非常长的密钥
    private static final String SECRET_KEY = "f4ds545f6a95s48a4s68d4a96w4fqs123z84g8ea4s486dg4t86j48u6g4k6l6sfg48es6hj4";

    private static final long EXPIRATION = 3600*24*3; // 三天
    //生成JWT令牌
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION*1000))
                .compact();
    }

    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
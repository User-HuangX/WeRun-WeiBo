package org.example.auth.utils;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {
    //一个非常长的密钥
    private static final String SECRET_KEY = "f4ds545f6a95s48a4s68d4a96w4fqs123z84g8ea4s486dg4t86j48u6g4k6l6sfg48es6hj4";
    //令牌有效的时长
    private static final long EXPIRATION = 60; // 1小时
    //生成JWT令牌
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION*1000))
                .compact();
    }
    //生成刷新JWT令牌
    public static String generateRefreshJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + 3600*1000))
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
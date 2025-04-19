package com.crypto.server.service;

import com.crypto.server.config.exceptions.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService{

    @Value("yJhY2zA6WxVr8PqWNxQtbk5U4v3iSz1A7ghz6j9kPZJXy9U2w")
    private String secretKey;

    public String generateToken(int id,  String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("_id", id);
        claims.put("username", username);

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject((String) claims.get("username"))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2592000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Object extractClaim(String token, String claimKey) {
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get(claimKey);
        } catch (JwtException e) {
          throw new UnauthorizedException("Invalid JWT token");
        }
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}


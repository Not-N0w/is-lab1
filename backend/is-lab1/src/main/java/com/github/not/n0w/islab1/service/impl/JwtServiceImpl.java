package com.github.not.n0w.islab1.service.impl;

import com.github.not.n0w.islab1.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final SecretKey secretKey;
    private final int accessTtlSec;
    private final int refreshTtlSec;

    public JwtServiceImpl(@Value("${security.jwt.secret.key}") String secret,
                          @Value("${security.jwt.expiration.sec}") int accessTtlSec,
                          @Value("${security.jwt.expiration.refresh.sec}") int refreshTtlSec) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTtlSec = accessTtlSec;
        this.refreshTtlSec = refreshTtlSec;
    }

    @Override
    public String extractUsername(String jwt) {
        return extractClaims(jwt).getPayload().getSubject();
    }


    private Jws<Claims> extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }

    @Override
    public String generateRefreshToken(String username) {
        return generateToken(username, "refresh");
    }

    @Override
    public String generateAccessToken(String username) {
        return generateToken(username, "access");
    }


    private String generateToken(String username, String type) {
        int expirationTime = type.equals("access") ? this.accessTtlSec : this.refreshTtlSec;
        Date expiryDate = Date.from(
                Instant.ofEpochMilli(System.currentTimeMillis() + expirationTime * 1000L)
        );

        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(Instant.now()))
                .claim("type", type)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token, UserDetails user, String type) {
        Jws<Claims> claims = extractClaims(token);
        String tokenType = claims.getPayload().get("type").toString();
        String username = claims.getPayload().getSubject();

        return username.equals(user.getUsername()) && tokenType.equals(type);
    }

}

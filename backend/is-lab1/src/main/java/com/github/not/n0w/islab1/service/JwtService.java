package com.github.not.n0w.islab1.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateAccessToken(String username);
    String generateRefreshToken(String username);
    String extractUsername(String jwt);
    boolean validateToken(String jwt, UserDetails user, String type);
}

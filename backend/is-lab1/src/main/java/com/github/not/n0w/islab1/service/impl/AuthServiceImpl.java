package com.github.not.n0w.islab1.service.impl;

import com.github.not.n0w.islab1.model.dto.AuthResponse;
import com.github.not.n0w.islab1.service.AuthService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;

    @Override
    public AuthResponse authenticate(String username, String password) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Wrong username or password");
        }
        return new AuthResponse(
                jwtService.generateAccessToken(username),
                jwtService.generateRefreshToken(username)
        );
    }

    @Override
    public String refreshToken(String refreshToken) {
        try {
            String username = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(refreshToken, userDetails, "refresh")) {
                return jwtService.generateAccessToken(username);
            }
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
            throw new BadCredentialsException("Invalid refresh token", e);
        }
        throw new BadCredentialsException("Invalid refresh token");
    }
}

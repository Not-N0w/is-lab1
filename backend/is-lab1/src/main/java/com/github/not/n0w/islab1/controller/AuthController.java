package com.github.not.n0w.islab1.controller;

import com.github.not.n0w.islab1.model.UserEntity;
import com.github.not.n0w.islab1.model.dto.AuthRequest;
import com.github.not.n0w.islab1.model.dto.AuthResponse;
import com.github.not.n0w.islab1.model.dto.RefreshRequest;
import com.github.not.n0w.islab1.service.AuthService;
import com.github.not.n0w.islab1.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/public/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest req) {
        return authService.authenticate(req.getUsername(), req.getPassword());
    }

    @PostMapping("/public/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody AuthRequest req) {
        userService.addUser(req.getUsername(), req.getPassword());
        return authService.authenticate(req.getUsername(), req.getPassword());
    }

    @PostMapping("/public/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshRequest req) {
        return new AuthResponse(authService.refreshToken(req.getRefreshToken()), req.getRefreshToken());
    }

    @GetMapping("/name")
    public String name(Principal principal) {
        return principal.getName();
    }
}
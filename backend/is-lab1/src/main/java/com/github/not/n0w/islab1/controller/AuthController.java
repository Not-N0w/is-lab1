package com.github.not.n0w.islab1.controller;

import com.github.not.n0w.islab1.model.UserEntity;
import com.github.not.n0w.islab1.model.dto.AuthRequest;
import com.github.not.n0w.islab1.model.dto.AuthResponse;
import com.github.not.n0w.islab1.model.dto.RefreshRequest;
import com.github.not.n0w.islab1.service.AuthService;
import com.github.not.n0w.islab1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/public/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {

        try {
            var response = authService.authenticate(authRequest.getUsername(), authRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/public/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest req) {
        UserEntity user = userService.addUser(req.getUsername(), req.getPassword());
        var response = authService.authenticate(user.getUsername(), user.getPasswordHash());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/public/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        try {
            return ResponseEntity.ok().body(
                    new AuthResponse(
                            authService.refreshToken(refreshRequest.getRefreshToken()),
                            refreshRequest.getRefreshToken()
                    )
            );
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}

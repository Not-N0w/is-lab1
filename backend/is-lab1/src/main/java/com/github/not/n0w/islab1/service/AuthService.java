package com.github.not.n0w.islab1.service;

import com.github.not.n0w.islab1.model.dto.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(String username, String password);
    String refreshToken(String refreshToken);
}

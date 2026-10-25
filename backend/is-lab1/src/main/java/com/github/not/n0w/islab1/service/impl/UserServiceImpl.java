package com.github.not.n0w.islab1.service.impl;

import com.github.not.n0w.islab1.model.UserEntity;
import com.github.not.n0w.islab1.repository.UserRepository;
import com.github.not.n0w.islab1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity addUser(String username, String password) {
        if(getUserByUsername(username) != null) {
            log.error("User with username {} already exists", username);
            throw new BadCredentialsException("User already exists");
        }

        UserEntity user = new UserEntity(
                username,
                passwordEncoder.encode(password)
                );
        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }
}

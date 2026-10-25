package com.github.not.n0w.islab1.service;

import com.github.not.n0w.islab1.model.UserEntity;

public interface UserService {
    UserEntity addUser(String username, String password);
    UserEntity getUserByUsername(String username);
}

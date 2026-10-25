package com.github.not.n0w.islab1.repository;

import com.github.not.n0w.islab1.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity getUserById(Integer id);
    UserEntity getUserByUsername(String username);
}

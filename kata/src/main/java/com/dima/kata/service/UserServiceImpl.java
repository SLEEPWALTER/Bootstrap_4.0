package com.dima.kata.service;

import com.dima.kata.models.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceImpl {
    User getUserById(Long id);
    void deleteUserById(Long id);
    User saveUser(User user);
    List<User> getAllUsers();
    Optional<User> getCurrentUser();
}

package com.dima.kata.service;

import com.dima.kata.models.User;
import com.dima.kata.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserServiceImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User saveUser(User user) {

        if (user.getId() == null && user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else if (user.getId() != null) {
            User existingUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                // Если пароль изменен - кодируем новый
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            user.setId(existingUser.getId());
        }

        if (user.getId() == null && user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Имя пользователя обязательно");
        }
        if (user.getAge() == null || user.getAge() < 0) {
            throw new RuntimeException("Возраст должен быть положительным");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email обязателен");
        }
        return userRepository.save(user);
    }

    @Transactional
    public User getUserById(Long id) {
        return  userRepository.findById(id)
                .map(user -> {
                    // Инициализируем LAZY коллекцию
                    user.getRoles().size();
                    return user;
                })
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с ID: " + id));
    }

    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }

}

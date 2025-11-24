package com.dima.kata.init;

import com.dima.kata.models.Role;
import  com.dima.kata.models.User;
import com.dima.kata.repository.RoleRepository;
import  com.dima.kata.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TestDataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Создаем роли если их нет
        createRoles();

        // Создаем тестовых пользователей
        createTestUsers();
    }

    private void createRoles() {
        if (roleRepository.count() == 0) {
            Role userRole = new Role("ROLE_USER");
            Role adminRole = new Role("ROLE_ADMIN");

            roleRepository.save(userRole);
            roleRepository.save(adminRole);
            System.out.println("Роли созданы: ROLE_USER, ROLE_ADMIN");
        }
    }

    private void createTestUsers() {
        // Создаем админа если его нет
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@example.com");
            admin.setAge(30);

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleRepository.findByName("ROLE_ADMIN").get());
            adminRoles.add(roleRepository.findByName("ROLE_USER").get());
            admin.setRoles(adminRoles);

            userRepository.save(admin);
            System.out.println("=== ТЕСТОВЫЕ ПОЛЬЗОВАТЕЛИ ===");
            System.out.println("Админ: admin / admin (роли: ADMIN, USER)");
        }

        // Создаем обычного пользователя если его нет
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setEmail("user@example.com");
            user.setAge(25);

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(roleRepository.findByName("ROLE_USER").get());
            user.setRoles(userRoles);

            userRepository.save(user);
            System.out.println("Пользователь: user / user (роль: USER)");
        }

        // Создаем тестового пользователя 2 если его нет
        if (userRepository.findByUsername("test").isEmpty()) {
            User test = new User();
            test.setUsername("test");
            test.setPassword(passwordEncoder.encode("test"));
            test.setEmail("test@example.com");
            test.setAge(22);

            Set<Role> testRoles = new HashSet<>();
            testRoles.add(roleRepository.findByName("ROLE_USER").get());
            test.setRoles(testRoles);

            userRepository.save(test);
            System.out.println("Тестовый: test / test (роль: USER)");
            System.out.println("===========================");
        }
    }
}
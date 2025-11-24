package com.dima.kata.controllers;

import com.dima.kata.models.User;
import com.dima.kata.repository.RoleRepository;
import com.dima.kata.service.UserService;
import com.dima.kata.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    private final UserServiceImpl userService;
    private final RoleRepository roleRepository;

    public HomeController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user")
    public String userPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", auth.getPrincipal());
        return "user";
    }

    @GetMapping("/admin")
    @Transactional(readOnly = true)
    public String adminPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", auth.getPrincipal());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("newUser", new User()); // для модального окна добавления
        return "users";
    }
}
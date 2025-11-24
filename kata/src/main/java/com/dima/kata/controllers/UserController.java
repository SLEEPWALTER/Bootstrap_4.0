package com.dima.kata.controllers;

import com.dima.kata.models.Role;
import com.dima.kata.models.User;
import com.dima.kata.repository.RoleRepository;
import com.dima.kata.service.UserService;
import com.dima.kata.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    private final UserServiceImpl userService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping()
    @Transactional(readOnly = true)
    public String home(Model model) {
        model.addAttribute("currentUser", userService.getCurrentUser().orElse(null));
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("newUser", new User()); // для модального окна добавления
        return "users";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {

        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> selectedRoles = new HashSet<>();
            for (Long roleId : roleIds) {
                roleRepository.findById(roleId).ifPresent(selectedRoles::add);
            }
            user.setRoles(selectedRoles);
        } else {
            // Если роли не выбраны, устанавливаем роль USER по умолчанию
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Роль USER не найдена"));
            user.setRoles(Set.of(userRole));
        }
        userService.saveUser(user);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUserById(id);
        return "redirect:/";
    }


}

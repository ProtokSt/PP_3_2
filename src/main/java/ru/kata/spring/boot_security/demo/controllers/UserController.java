package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.BaseRolesInit;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('ROLE_USER')")
public class UserController {
    private final UserService userService;
    private final BaseRolesInit baseRolesInit;

    @Autowired
    public UserController(UserService userService, BaseRolesInit baseRolesInit) {
        this.userService = userService;
        this.baseRolesInit = baseRolesInit;
    }

    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        System.out.println("user GET");
        List<User> accessUsers = new ArrayList<>();
        accessUsers.add(userService.findByUsername(principal.getName()));
        model.addAttribute("allUsers", accessUsers);
        return "/user";
    }
}

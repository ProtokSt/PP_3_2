package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.BaseRolesInit;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {
    private final UserService userService;

    private final BaseRolesInit baseRolesInit;

    @Autowired
    public AuthController(UserService userService, BaseRolesInit baseRolesInit) {
        this.userService = userService;
        this.baseRolesInit = baseRolesInit;
    }

    @GetMapping("/login")
    public String loginPage() {
        System.out.println("login GET");
        return "/login";
    }
    // Метод POST со страницы перехватывает Spring Security

    // Инициализация первичных ролей доступа, дублирование исключено
    @RequestMapping("/baseRolesInitiation")
    public String generateUsers() {

        System.out.println("Пропись базовых ролей и пользователей: " + baseRolesInit.doInit());
        return "/login";
    }

}

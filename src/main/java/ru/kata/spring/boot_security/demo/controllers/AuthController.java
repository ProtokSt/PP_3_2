package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.BaseRolesInit;


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

        // тест кодировщика. онлайн кодировщики дают неверные результаты
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String password = "user";
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println();
        System.out.println("Password is         : " + password);
        System.out.println("Encoded Password is : " + encodedPassword);
        System.out.println();
        boolean isPasswordMatch = passwordEncoder.matches(password, encodedPassword);
        System.out.println("Password : " + password + "   isPasswordMatch    : " + isPasswordMatch);

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

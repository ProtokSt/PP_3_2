package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // отображение всех
    @GetMapping("/showAllUsers")
    public String showAllUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);

        return "/admin/all-users";
    }

    // добавление нового
    @GetMapping("/addNewUser")
    public String addNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        return "/admin/user-info";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user) {
            userService.addUser(user);
        return "redirect:/admin/showAllUsers";
    }

    // редактирование существующего
    @GetMapping("/updateInfo/{id}")
    public String updateInfo(@ModelAttribute("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);

        return "/admin/update-user";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin/showAllUsers";
    }

    // удаление
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@ModelAttribute("id") Long id) {
        userService.removeUser(id);

        return "redirect:/admin/showAllUsers";
    }

    // генерация
    @RequestMapping("/generateUsers")
    public String generateUsers() {

        User user1 = new User("Simba", "Pecking", "QWE", 1100);
        User user2 = new User("Ратмир", "Усманов", "ПАТ", 1200);
        User user3 = new User("Кира", "Ван Вогт", "QAZ", 1300);
        User user4 = new User("Александр", "Севостьянов", "Kata1", 1400);
        User user5 = new User("Джонсон", "Трал", "МЕХ", 1502);
        User user6 = new User("Германикус", "Из Стратона", "TP2", 1600);

        Set<Role> userRole = new HashSet<>();
        userRole.add(roleService.getRoleById(1L));

        user1.setRoles(userRole);
        user2.setRoles(userRole);
        user3.setRoles(userRole);
        user4.setRoles(userRole);
        user5.setRoles(userRole);
        user6.setRoles(userRole);

        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        userService.addUser(user4);
        userService.addUser(user5);
        userService.addUser(user6);

        return "redirect:/admin/showAllUsers";
    }

}

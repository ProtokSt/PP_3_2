package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.util.List;

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

    // добавление нового.
    // Пояснение: Метод обрабатывает логику нажатия кнопки "Добавить нового пользователя" во фронт интерфейсе
    // панели администратора. Непосредственно передаёт вновь созданный объект - экземпляр класса User в фронт
    // интерфейс для создания формы и последующего заполнения полей экземпляра введёнными на фронте данными.
    @GetMapping("/addNewUser")
    public String addNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        return "/admin/user-info";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/user-info";
        }
        System.out.println("userService.findByUsername(" + user.getUsername() + ") = " + userService.findByUsername(user.getUsername()));
        if (userService.findByUsername(user.getUsername()) != null) {
            bindingResult.addError(new FieldError("username", "username",
                    String.format("Запись в БД с таким именем \"%s\" уже существует.!", user.getUsername())));
            return "/admin/user-info";
        }
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
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult) {

        // при обновлении необходимо также считывать добавленные в этой сессии данные,
        // для этого @Transactional помещается в сервисы вместо репозиториев.
        if (bindingResult.hasErrors()) {
            return "/admin/update-user";
        }
        System.out.println("userService.findByUsername(" + user.getUsername() + ") = " + userService.findByUsername(user.getUsername()));
        if (userService.findByUsername(user.getUsername()) != null) {
            bindingResult.addError(new FieldError("username", "username",
                    String.format("Запись в БД с таким именем \"%s\" уже существует.!", user.getUsername())));
            return "/admin/update-user";
        }

        userService.updateUser(user);
        return "redirect:/admin/showAllUsers";
    }

    // удаление
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@ModelAttribute("id") Long id) {
        userService.removeUser(id);

        return "redirect:/admin/showAllUsers";
    }

    // генерация. Здесь была логика из предыдущего проекта для добавления нескольких записей в бд одним нажатием.
    // ментор рекомендовал убрать этот код.

}

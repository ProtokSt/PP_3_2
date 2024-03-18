package ru.kata.spring.boot_security.demo.repositories;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    User getUserById(Long id);

    User findByUsername(String username);

    void addUser(User user);

    void removeUser(Long id);

    void updateUser(User user);
}

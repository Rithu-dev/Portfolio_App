package com.portfolio.portfolio_app.controller;

import java.util.List;
import com.portfolio.portfolio_app.entity.User;
import com.portfolio.portfolio_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.portfolio.portfolio_app.service.CurrentUserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public User getCurrentUser() {
        return currentUserService.getCurrentUser();
    }

    // Create
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // GET all
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Update
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

}
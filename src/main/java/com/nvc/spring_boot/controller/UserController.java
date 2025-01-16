package com.nvc.spring_boot.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nvc.spring_boot.entity.User;
import com.nvc.spring_boot.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/list")
    public List<User> getList() {
        return userService.getList();
    }

    @GetMapping("/user/{id}")
    public User findUser(@PathVariable("id") Long id) {
        return userService.findUser(id);
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
    
    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return newUser;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "Delete User";
    }
}

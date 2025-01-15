package com.nvc.spring_boot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nvc.spring_boot.entity.User;

@RestController
public class UserController {

    @GetMapping("/user/create")
    public String create() {
        User user = new User();
        user.setEmail("ngocuong@gmail.com");
        user.setName("Cuong");
        user.setPassword("123456");
        return "create user";
    }
}

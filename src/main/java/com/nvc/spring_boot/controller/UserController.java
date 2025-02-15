package com.nvc.spring_boot.controller;

import java.util.List;
import java.util.Optional;

import com.nvc.spring_boot.domain.dto.PaginationDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.nvc.spring_boot.domain.User;
import com.nvc.spring_boot.service.UserService;
import com.nvc.spring_boot.util.error.IdInvalidException;


@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users/list")
    public ResponseEntity<PaginationDTO> getList(
            @RequestParam("page") Optional<String> pageOptional,
            @RequestParam("limit") Optional<String> limitOptional
    ) {
        String page = pageOptional.isPresent() ? pageOptional.get() : "";
        String limit = limitOptional.isPresent() ? limitOptional.get() : "";
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(limit));
        return ResponseEntity.ok(userService.getList(pageable));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> findUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findUser(id));
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }
    
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        if (id >= 100) {
            throw new IdInvalidException("Id must be less than 100");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok(null);
    }
}

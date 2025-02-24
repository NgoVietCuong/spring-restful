package com.nvc.spring_boot.controller;

import com.nvc.spring_boot.domain.dto.PaginationDTO;
import com.nvc.spring_boot.domain.dto.ResCreateUserDTO;
import com.nvc.spring_boot.domain.dto.ResUpdateUserDTO;
import com.nvc.spring_boot.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nvc.spring_boot.domain.User;
import com.nvc.spring_boot.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    @ApiMessage("get user list")
    public ResponseEntity<PaginationDTO> getList(
            @Filter Specification<User> specification,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getList(specification, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("get user info")
    public ResponseEntity<User> findUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findUser(id));
    }

    @PutMapping()
    @ApiMessage("update user info")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }
    
    @PostMapping()
    @ApiMessage("create new user")
    public ResponseEntity<ResCreateUserDTO> createUser(@RequestBody User user)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

package com.nvc.spring_boot.controller;

import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.dto.user.request.CreateUserRequest;
import com.nvc.spring_boot.dto.user.request.UpdateUserRequest;
import com.nvc.spring_boot.dto.user.response.CreateUserResponse;
import com.nvc.spring_boot.dto.user.response.UpdateUserResponse;
import com.nvc.spring_boot.dto.user.response.FindUserResponse;
import com.nvc.spring_boot.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nvc.spring_boot.entity.User;
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
    public ResponseEntity<PaginationResponse> getList(
            @Filter Specification<User> specification,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getList(specification, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("get user info")
    public ResponseEntity<FindUserResponse> findUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findUser(id));
    }

    @PutMapping()
    @ApiMessage("update user info")
    public ResponseEntity<UpdateUserResponse> updateUser(@Valid @RequestBody UpdateUserRequest user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }
    
    @PostMapping()
    @ApiMessage("create new user")
    public ResponseEntity<CreateUserResponse> createUser(@Valid@RequestBody CreateUserRequest user)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete user")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

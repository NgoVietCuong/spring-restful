package com.nvc.spring_boot.dto.user.request;

import com.nvc.spring_boot.entity.Company;
import com.nvc.spring_boot.util.constant.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotNull(message = "Email must not be empty")
    private String email;

    private String name;

    @NotNull(message = "Password must not be empty")
    private String password;

    private Gender gender;
    private String address;
    private Company company;
}

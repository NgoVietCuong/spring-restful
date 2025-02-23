package com.nvc.spring_boot.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "Email must not be empty")
    private String email;

    @NotBlank(message = "Password must not be empty")
    private String password;
}

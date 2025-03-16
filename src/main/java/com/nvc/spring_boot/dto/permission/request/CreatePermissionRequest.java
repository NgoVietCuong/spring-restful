package com.nvc.spring_boot.dto.permission.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePermissionRequest {
    @NotBlank(message = "Name must not be empty")
    private String name;

    @NotBlank(message = "Method must not be empty")
    private String method;

    @NotBlank(message = "ApiPath must not be empty")
    private String apiPath;

    @NotBlank(message = "Module must not be empty")
    private String module;
}

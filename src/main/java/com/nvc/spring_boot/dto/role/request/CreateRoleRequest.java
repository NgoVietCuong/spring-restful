package com.nvc.spring_boot.dto.role.request;

import com.nvc.spring_boot.entity.Permission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateRoleRequest {
    @NotBlank(message = "Name must not be empty")
    private String name;

    private String description;
    private Boolean isActive;

    @NotNull(message = "Permissions must not be empty")
    private List<Permission> permissions;
}

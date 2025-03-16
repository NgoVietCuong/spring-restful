package com.nvc.spring_boot.dto.permission.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePermissionRequest {
    @NotNull(message = "Id must not be empty")
    private Long id;

    @NotBlank(message = "Permission name must not be empty")
    private String name;

    @NotBlank(message = "Permission method must not be empty")
    private String method;

    @NotBlank(message = "Permission apiPath must not be empty")
    private String apiPath;

    @NotBlank(message = "Permission module must not be empty")
    private String module;
}

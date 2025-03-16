package com.nvc.spring_boot.dto.role.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateRoleRequest extends CreateRoleRequest {
    @NotNull(message = "Id must not be empty")
    private Long id;
}

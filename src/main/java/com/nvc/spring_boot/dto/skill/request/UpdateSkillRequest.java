package com.nvc.spring_boot.dto.skill.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateSkillRequest {
    @NotNull(message = "Id must not be empty")
    private Long id;

    @NotBlank(message = "Name must not be empty")
    private String name;
}

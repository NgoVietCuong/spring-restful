package com.nvc.spring_boot.dto.skill.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSkillRequest {
    @NotBlank(message = "Skill name must not be empty")
    private String name;
}

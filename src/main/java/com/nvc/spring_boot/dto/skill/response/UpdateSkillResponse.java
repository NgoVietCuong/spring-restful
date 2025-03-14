package com.nvc.spring_boot.dto.skill.response;

import lombok.Data;

import java.time.Instant;

@Data
public class UpdateSkillResponse {
    private Long id;
    private String name;
    private Instant updatedAt;
}

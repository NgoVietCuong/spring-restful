package com.nvc.spring_boot.dto.skill.response;

import lombok.Data;

import java.time.Instant;

@Data
public class FindSkillResponse {
    private Long id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
}

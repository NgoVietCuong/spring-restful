package com.nvc.spring_boot.dto.role.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class CreateRoleResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
    private Instant createdAt;
    private List<RolePermission> permissions;

    @Data
    @Builder
    public static class RolePermission {
        private Long id;
        private String name;
    }
}

package com.nvc.spring_boot.dto.permission.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UpdatePermissionResponse {
    private Long id;
    private String name;
    private String apiPath;
    private String method;
    private String module;
    private Instant updatedAt;
}

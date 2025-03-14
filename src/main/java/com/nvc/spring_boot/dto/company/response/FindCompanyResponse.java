package com.nvc.spring_boot.dto.company.response;

import lombok.Data;

import java.time.Instant;

@Data
public class FindCompanyResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String logo;
    private Instant createdAt;
    private Instant updatedAt;
}

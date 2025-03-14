package com.nvc.spring_boot.dto.company.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCompanyRequest {
    @NotNull(message = "Id must not be empty")
    private Long id;

    private String name;
    private String description;
    private String address;
    private String logo;
}

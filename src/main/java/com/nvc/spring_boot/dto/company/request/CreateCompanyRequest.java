package com.nvc.spring_boot.dto.company.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCompanyRequest {
    @NotBlank(message = "Company name must not be empty")
    private String name;

    private String description;
    private String address;
    private String logo;
}

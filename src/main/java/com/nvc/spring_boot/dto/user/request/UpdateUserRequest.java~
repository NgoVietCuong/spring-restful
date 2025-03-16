package com.nvc.spring_boot.dto.user.request;

import com.nvc.spring_boot.entity.Company;
import com.nvc.spring_boot.util.constant.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotNull(message = "Id must not be empty")
    private Long id;
    private String name;
    private String address;
    private Gender gender;
    private Company company;
}

package com.nvc.spring_boot.dto.job.request;

import com.nvc.spring_boot.entity.Company;
import com.nvc.spring_boot.entity.Skill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UpdateJobRequest {
    @NotNull(message = "Id must not be empty")
    private Long id;

    private String name;
    private String location;
    private Double salary;
    private Integer quantity;
    private String description;
    private Boolean isActive;
    private Date startDate;
    private Date endDate;
    private Company company;
    private List<Skill> skills;
}

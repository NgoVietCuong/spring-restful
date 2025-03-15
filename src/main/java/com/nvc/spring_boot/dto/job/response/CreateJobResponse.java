package com.nvc.spring_boot.dto.job.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
public class CreateJobResponse {
    private Long id;
    private String location;
    private Double salary;
    private Integer quantity;
    private String description;
    private Boolean isActive;
    private Date startDate;
    private Date endDate;
    private Instant createdAt;
    private JobCompany company;
    private List<JobSkill> skills;

    @Data
    @Builder
    @AllArgsConstructor
    public static class JobCompany {
        private Long id;
        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class JobSkill {
        private Long id;
        private String name;
    }
}

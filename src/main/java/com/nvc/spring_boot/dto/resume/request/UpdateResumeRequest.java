package com.nvc.spring_boot.dto.resume.request;

import com.nvc.spring_boot.util.constant.ResumeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateResumeRequest {
    @NotNull(message = "Id must not be empty")
    private Long id;

    @NotNull(message = "Status must not be empty")
    private ResumeStatus status;
}

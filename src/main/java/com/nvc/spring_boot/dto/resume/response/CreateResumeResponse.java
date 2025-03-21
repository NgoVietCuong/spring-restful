package com.nvc.spring_boot.dto.resume.response;

import com.nvc.spring_boot.util.constant.ResumeStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CreateResumeResponse {
    private Long id;
    private String url;
    private ResumeStatus status;
    private Instant createdAt;
}

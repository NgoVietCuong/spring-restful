package com.nvc.spring_boot.dto.resume.response;

import com.nvc.spring_boot.util.constant.ResumeStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class FindResumeResponse {
    private Long id;
    private String url;
    private String email;
    private ResumeStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private ResumeUser user;
    private ResumeJob job;

    @Data
    @Builder
    public static class ResumeUser {
        private Long id;
        private String name;
    }

    @Data
    @Builder
    public static class ResumeJob {
        private Long id;
        private String name;
    }
}

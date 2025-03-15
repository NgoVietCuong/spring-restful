package com.nvc.spring_boot.dto.resume.request;

import com.nvc.spring_boot.entity.Job;
import com.nvc.spring_boot.entity.User;
import com.nvc.spring_boot.util.constant.ResumeStatus;
import lombok.Data;

@Data
public class CreateResumeRequest {
    private String email;
    private String url;
    private ResumeStatus status;
    private User user;
    private Job job;
}

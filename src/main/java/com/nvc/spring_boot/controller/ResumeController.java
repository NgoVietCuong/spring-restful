package com.nvc.spring_boot.controller;

import com.nvc.spring_boot.domain.Resume;
import com.nvc.spring_boot.service.ResumeService;
import com.nvc.spring_boot.util.annotation.ApiMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resumes")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping()
    @ApiMessage("create new resume")
    public ResponseEntity<Resume> createResume(Resume resume) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeService.createResume(resume));
    }

}

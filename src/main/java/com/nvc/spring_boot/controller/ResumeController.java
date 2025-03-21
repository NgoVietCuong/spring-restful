package com.nvc.spring_boot.controller;

import com.nvc.spring_boot.dto.resume.request.CreateResumeRequest;
import com.nvc.spring_boot.dto.resume.request.UpdateResumeRequest;
import com.nvc.spring_boot.dto.resume.response.CreateResumeResponse;
import com.nvc.spring_boot.dto.resume.response.FindResumeResponse;
import com.nvc.spring_boot.dto.resume.response.UpdateResumeResponse;
import com.nvc.spring_boot.entity.Resume;
import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.service.ResumeService;
import com.nvc.spring_boot.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resumes")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping("/list")
    @ApiMessage("get resume list")
    public ResponseEntity<PaginationResponse> getList(
            @Filter Specification<Resume> specification,
            Pageable pageable) {
        return ResponseEntity.ok(resumeService.getList(specification, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("get resume info")
    public ResponseEntity<FindResumeResponse> findResume(@PathVariable("id") Long id) {
        return ResponseEntity.ok(resumeService.findOne(id));
    }

    @PostMapping()
    @ApiMessage("create new resume")
    public ResponseEntity<CreateResumeResponse> createResume(@Valid @RequestBody CreateResumeRequest resume) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeService.createResume(resume));
    }

    @PutMapping()
    @ApiMessage("update resume")
    public ResponseEntity<UpdateResumeResponse> updateResume(@Valid @RequestBody UpdateResumeRequest resume) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeService.updateResume(resume));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete resume")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}

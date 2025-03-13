package com.nvc.spring_boot.controller;

import com.nvc.spring_boot.entity.Job;
import com.nvc.spring_boot.dto.PaginationDTO;
import com.nvc.spring_boot.service.JobService;
import com.nvc.spring_boot.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/list")
    @ApiMessage("get job list")
    public ResponseEntity<PaginationDTO> getList(
            @Filter Specification<Job> specification,
            Pageable pageable) {
        return ResponseEntity.ok(jobService.getList(specification, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("get job info")
    public ResponseEntity<Job> findJob(@PathVariable("id") Long id) {
        return ResponseEntity.ok(jobService.findJob(id));
    }

    @PostMapping()
    @ApiMessage("create new job")
    public ResponseEntity<Job> createJob(@Valid @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(job));
    }

    @PutMapping()
    @ApiMessage("update job info")
    public ResponseEntity<Job> updateJob(@Valid @RequestBody Job job) {
        return ResponseEntity.ok(jobService.updateJob(job));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete job")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

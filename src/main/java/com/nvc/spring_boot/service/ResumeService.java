package com.nvc.spring_boot.service;

import com.nvc.spring_boot.dto.resume.request.CreateResumeRequest;
import com.nvc.spring_boot.dto.resume.request.UpdateResumeRequest;
import com.nvc.spring_boot.dto.resume.response.CreateResumeResponse;
import com.nvc.spring_boot.dto.resume.response.FindResumeResponse;
import com.nvc.spring_boot.dto.resume.response.UpdateResumeResponse;
import com.nvc.spring_boot.entity.Job;
import com.nvc.spring_boot.entity.Resume;
import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.entity.User;
import com.nvc.spring_boot.repository.JobRepository;
import com.nvc.spring_boot.repository.ResumeRepository;
import com.nvc.spring_boot.repository.UserRepository;
import com.nvc.spring_boot.util.error.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public PaginationResponse getList(Specification<Resume> specification, Pageable pageable) {
        Page<Resume> pageResume = resumeRepository.findAll(specification, pageable);
        PaginationResponse.PaginationMeta meta = PaginationResponse.PaginationMeta.builder()
                .currentPage(pageable.getPageNumber() + 1)
                .itemsPerPage(pageable.getPageSize())
                .totalPages(pageResume.getTotalPages())
                .totalItems(pageResume.getTotalElements())
                .build();

        List<FindResumeResponse> resResumes = pageResume.getContent().stream().map(item -> {
            FindResumeResponse.ResumeUser user = FindResumeResponse.ResumeUser.builder()
                    .id(item.getUser().getId())
                    .name(item.getUser().getName())
                    .build();

            FindResumeResponse.ResumeJob job = FindResumeResponse.ResumeJob.builder()
                    .id(item.getJob().getId())
                    .name(item.getJob().getName())
                    .build();

            return FindResumeResponse.builder()
                    .id(item.getId())
                    .url(item.getUrl())
                    .email(item.getEmail())
                    .status(item.getStatus())
                    .createdAt(item.getCreatedAt())
                    .updatedAt(item.getUpdatedAt())
                    .user(user)
                    .job(job)
                    .build();
        }).toList();

        return PaginationResponse.builder()
                .meta(meta)
                .content(resResumes)
                .build();
    }

    public FindResumeResponse findOne(Long id) {
        Resume resume = resumeRepository.findById(id).orElse(null);

        if (resume != null) {
            FindResumeResponse.ResumeUser user = FindResumeResponse.ResumeUser.builder()
                    .id(resume.getUser().getId())
                    .name(resume.getUser().getName())
                    .build();

            FindResumeResponse.ResumeJob job = FindResumeResponse.ResumeJob.builder()
                    .id(resume.getJob().getId())
                    .name(resume.getJob().getName())
                    .build();

            return FindResumeResponse.builder()
                    .id(resume.getId())
                    .url(resume.getUrl())
                    .email(resume.getEmail())
                    .status(resume.getStatus())
                    .createdAt(resume.getCreatedAt())
                    .updatedAt(resume.getUpdatedAt())
                    .user(user)
                    .job(job)
                    .build();
        }

        return null;
    }

    public CreateResumeResponse createResume(CreateResumeRequest resume) {
        if (resume.getUser() != null) {
            User user = userRepository.findById(resume.getUser().getId()).orElse(null);
            resume.setUser(user);
        }

        if (resume.getJob() != null) {
            Job job = jobRepository.findById(resume.getJob().getId()).orElse(null);
            resume.setJob(job);
        }

        Resume newResume = Resume.builder()
                        .url(resume.getUrl())
                        .email(resume.getEmail())
                        .status(resume.getStatus())
                        .user(resume.getUser())
                        .build();

        resumeRepository.save(newResume);

        return CreateResumeResponse.builder()
                .id(newResume.getId())
                .url(newResume.getUrl())
                .status(newResume.getStatus())
                .createdAt(newResume.getCreatedAt())
                .build();
    }

    public UpdateResumeResponse updateResume(UpdateResumeRequest resumeData) {
        Resume resume = resumeRepository.findById(resumeData.getId()).orElse(null);
        if (resume == null) throw new ResourceNotFoundException("Resume not found");

        resume.setStatus(resumeData.getStatus());
        resumeRepository.save(resume);

        return UpdateResumeResponse.builder()
                .id(resume.getId())
                .url(resume.getUrl())
                .status(resume.getStatus())
                .updatedAt(resume.getUpdatedAt())
                .build();
    }

    public void deleteResume(Long id) {
        resumeRepository.deleteById(id);
    }
}

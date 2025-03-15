package com.nvc.spring_boot.service;

import com.nvc.spring_boot.dto.job.request.CreateJobRequest;
import com.nvc.spring_boot.dto.job.request.UpdateJobRequest;
import com.nvc.spring_boot.dto.job.response.CreateJobResponse;
import com.nvc.spring_boot.dto.job.response.UpdateJobResponse;
import com.nvc.spring_boot.entity.Company;
import com.nvc.spring_boot.entity.Job;
import com.nvc.spring_boot.entity.Skill;
import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.repository.CompanyRepository;
import com.nvc.spring_boot.repository.JobRepository;
import com.nvc.spring_boot.repository.SkillRepository;
import com.nvc.spring_boot.util.error.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public PaginationResponse getList(Specification<Job> specification, Pageable pageable) {
        Page<Job> pageJob = jobRepository.findAll(specification, pageable);
        PaginationResponse.PaginationMeta meta = PaginationResponse.PaginationMeta.builder()
                .currentPage(pageable.getPageNumber() + 1)
                .itemsPerPage(pageable.getPageSize())
                .totalPages(pageJob.getTotalPages())
                .totalItems(pageJob.getTotalElements())
                .build();

        return PaginationResponse.builder()
                .meta(meta)
                .content(pageJob.getContent())
                .build();
    }

    public Job findJob(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    public UpdateJobResponse updateJob(UpdateJobRequest jobData) throws ResourceNotFoundException {
        Job job = findJob(jobData.getId());
        if (job == null) throw new ResourceNotFoundException("Job not found");

        BeanUtils.copyProperties(jobData, job);
        if (jobData.getCompany() != null) {
            Company company = companyRepository.findById(jobData.getCompany().getId()).orElse(null);
            job.setCompany(company);
        }

        if (jobData.getSkills() != null) {
            List<Skill> skills =
                    skillRepository.findSkillByIdIn(jobData.getSkills().stream().map(Skill::getId).collect(Collectors.toList()));
            job.setSkills(skills);
        }

        jobRepository.save(job);

        UpdateJobResponse.JobCompany jobCompany = job.getCompany() != null ? UpdateJobResponse.JobCompany.builder()
                .id(job.getCompany().getId())
                .name(job.getCompany().getName())
                .build() : null;

        List<UpdateJobResponse.JobSkill> jobSkills = job.getSkills().stream().map(skill -> {
            return UpdateJobResponse.JobSkill.builder()
                    .id(skill.getId())
                    .name(skill.getName())
                    .build();
        }).toList();

        UpdateJobResponse resUpdateJob = new UpdateJobResponse();
        BeanUtils.copyProperties(job, resUpdateJob);
        resUpdateJob.setCompany(jobCompany);
        resUpdateJob.setSkills(jobSkills);
        return resUpdateJob;
    }

    public CreateJobResponse createJob(CreateJobRequest jobData) {
        Company company = jobData.getCompany() != null ? companyRepository.findById(jobData.getCompany().getId()).orElse(null) : null;

        List<Skill> skills = jobData.getSkills() != null ?
                skillRepository.findSkillByIdIn(jobData.getSkills().stream().map(Skill::getId).collect(Collectors.toList())) : Collections.emptyList();

        Job newJob = Job.builder()
                .name(jobData.getName())
                .location(jobData.getLocation())
                .salary(jobData.getSalary())
                .quantity(jobData.getQuantity())
                .description(jobData.getDescription())
                .startDate(jobData.getStartDate())
                .endDate(jobData.getEndDate())
                .isActive(jobData.getIsActive())
                .company(company)
                .skills(skills)
                .build();

        jobRepository.save(newJob);

        CreateJobResponse.JobCompany jobCompany = newJob.getCompany() != null ? CreateJobResponse.JobCompany.builder()
                                                                                    .id(newJob.getCompany().getId())
                                                                                    .name(newJob.getCompany().getName())
                                                                                    .build() : null;

        List<CreateJobResponse.JobSkill> jobSkills = newJob.getSkills().stream().map(skill -> {
             return CreateJobResponse.JobSkill.builder()
                    .id(skill.getId())
                    .name(skill.getName())
                    .build();
        }).toList();

        CreateJobResponse resCreateJob = new CreateJobResponse();
        BeanUtils.copyProperties(newJob, resCreateJob);
        resCreateJob.setCompany(jobCompany);
        resCreateJob.setSkills(jobSkills);

        return resCreateJob;

    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}

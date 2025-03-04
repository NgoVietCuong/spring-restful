package com.nvc.spring_boot.service;

import com.nvc.spring_boot.domain.Company;
import com.nvc.spring_boot.domain.Job;
import com.nvc.spring_boot.domain.Skill;
import com.nvc.spring_boot.domain.response.PaginationDTO;
import com.nvc.spring_boot.repository.CompanyRepository;
import com.nvc.spring_boot.repository.JobRepository;
import com.nvc.spring_boot.repository.SkillRepository;
import com.nvc.spring_boot.util.error.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    public PaginationDTO getList(Specification<Job> specification, Pageable pageable) {
        Page<Job> pageCompany = jobRepository.findAll(specification, pageable);
        PaginationDTO result = new PaginationDTO();
        PaginationDTO.MetaDTO meta = new PaginationDTO.MetaDTO();

        meta.setCurrentPage(pageable.getPageNumber() + 1);
        meta.setItemsPerPage(pageable.getPageSize());
        meta.setTotalPages(pageCompany.getTotalPages());
        meta.setTotalItems(pageCompany.getTotalElements());

        result.setMeta(meta);
        result.setContent(pageCompany.getContent());

        return result;
    }

    public Job findJob(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    public Job updateJob(Job jobData) throws ResourceNotFoundException {
        Job job = findJob(jobData.getId());
        if (job == null) throw new ResourceNotFoundException("Job not found");

        BeanUtils.copyProperties(jobData, job);
        if (job.getCompany() != null) {
            Company company = companyRepository.findById(job.getCompany().getId()).orElse(null);
            job.setCompany(company);
        }

        if (job.getSkills() != null) {
            List<Skill> skills =
                    skillRepository.findSkillByIdIn(job.getSkills().stream().map(Skill::getId).collect(Collectors.toList()));
            job.setSkills(skills);
        }

        return jobRepository.save(job);
    }

    public Job createJob(Job job) {
        Company company = companyRepository.findById(job.getCompany().getId()).orElse(null);
        job.setCompany(company);

        List<Skill> skills =
                skillRepository.findSkillByIdIn(job.getSkills().stream().map(Skill::getId).collect(Collectors.toList()));
        job.setSkills(skills);
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}

package com.nvc.spring_boot.service;

import com.nvc.spring_boot.entity.Resume;
import com.nvc.spring_boot.dto.PaginationDTO;
import com.nvc.spring_boot.repository.ResumeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public PaginationDTO getList(Specification<Resume> specification, Pageable pageable) {
        Page<Resume> pageCompany = resumeRepository.findAll(specification, pageable);
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

    public Resume findOne(Long id) {
        return resumeRepository.findById(id).orElse(null);
    }

    public Resume createResume(Resume resume) {
        return resumeRepository.save(resume);
    }

}

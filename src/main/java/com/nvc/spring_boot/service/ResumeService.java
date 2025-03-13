package com.nvc.spring_boot.service;

import com.nvc.spring_boot.entity.Resume;
import com.nvc.spring_boot.dto.PaginationResponse;
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

    public PaginationResponse getList(Specification<Resume> specification, Pageable pageable) {
        Page<Resume> pageResume = resumeRepository.findAll(specification, pageable);
        PaginationResponse.PaginationMeta meta = PaginationResponse.PaginationMeta.builder()
                .currentPage(pageable.getPageNumber() + 1)
                .itemsPerPage(pageable.getPageSize())
                .totalPages(pageResume.getTotalPages())
                .totalItems(pageResume.getTotalElements())
                .build();

        return PaginationResponse.builder()
                .meta(meta)
                .content(pageResume.getContent())
                .build();
    }

    public Resume findOne(Long id) {
        return resumeRepository.findById(id).orElse(null);
    }

    public Resume createResume(Resume resume) {
        return resumeRepository.save(resume);
    }

}

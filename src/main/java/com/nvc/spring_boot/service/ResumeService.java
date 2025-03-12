package com.nvc.spring_boot.service;

import com.nvc.spring_boot.domain.Resume;
import com.nvc.spring_boot.repository.ResumeRepository;
import org.springframework.stereotype.Service;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public Resume createResume(Resume resume) {
        return resumeRepository.save(resume);
    }

}

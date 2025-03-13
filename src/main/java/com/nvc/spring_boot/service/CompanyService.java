package com.nvc.spring_boot.service;

import com.nvc.spring_boot.entity.Company;
import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.repository.CompanyRepository;
import com.nvc.spring_boot.repository.JobRepository;
import com.nvc.spring_boot.repository.UserRepository;
import com.nvc.spring_boot.util.error.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public PaginationResponse getList(Specification<Company> specification, Pageable pageable) {
        Page<Company> pageCompany = companyRepository.findAll(specification, pageable);
        PaginationResponse.PaginationMeta meta = PaginationResponse.PaginationMeta.builder()
                .currentPage(pageable.getPageNumber() + 1)
                .itemsPerPage(pageable.getPageSize())
                .totalPages(pageCompany.getTotalPages())
                .totalItems(pageCompany.getTotalElements())
                .build();

        return PaginationResponse.builder()
                .meta(meta)
                .content(pageCompany.getContent())
                .build();
    }

    public Company findCompany(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    public Company updateCompany(Company companyData) throws ResourceNotFoundException {
        Company company = findCompany(companyData.getId());
        if (company != null) {
            company.setName(companyData.getName());
            company.setDescription(companyData.getDescription());
            company.setAddress(companyData.getAddress());
            company.setLogo(companyData.getLogo());

            return companyRepository.save(company);
        } else {
            throw new ResourceNotFoundException("Company not found");
        }
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        Company company = findCompany(id);
        if (company != null) {
            userRepository.deleteAllByCompany(company);
            jobRepository.deleteAllByCompany(company);
        }
        companyRepository.deleteById(id);
    }
}

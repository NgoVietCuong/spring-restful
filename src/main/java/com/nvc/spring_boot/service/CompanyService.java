package com.nvc.spring_boot.service;

import com.nvc.spring_boot.dto.company.request.CreateCompanyRequest;
import com.nvc.spring_boot.dto.company.request.UpdateCompanyRequest;
import com.nvc.spring_boot.dto.company.response.CreateCompanyResponse;
import com.nvc.spring_boot.dto.company.response.FindCompanyResponse;
import com.nvc.spring_boot.dto.company.response.UpdateCompanyResponse;
import com.nvc.spring_boot.dto.user.response.FindUserResponse;
import com.nvc.spring_boot.entity.Company;
import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.repository.CompanyRepository;
import com.nvc.spring_boot.repository.JobRepository;
import com.nvc.spring_boot.repository.UserRepository;
import com.nvc.spring_boot.util.error.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        List<FindCompanyResponse> resCompanies = pageCompany.getContent().stream().map(item -> {
            FindCompanyResponse resCompany = new FindCompanyResponse();
            BeanUtils.copyProperties(item, resCompany);

            return resCompany;
        }).toList();

        return PaginationResponse.builder()
                .meta(meta)
                .content(resCompanies)
                .build();
    }

    public FindCompanyResponse findCompany(Long id) {
        Company company = companyRepository.findById(id).orElse(null);

        if (company != null) {
            FindCompanyResponse resCompany = new FindCompanyResponse();
            BeanUtils.copyProperties(company, resCompany);

            return resCompany;
        }

        return null;
    }

    public UpdateCompanyResponse updateCompany(UpdateCompanyRequest companyData) throws ResourceNotFoundException {
        Company company = companyRepository.findById(companyData.getId()).orElse(null);
        if (company != null) {
            company.setName(Optional.ofNullable(companyData.getName()).orElse(company.getName()));
            company.setDescription(Optional.ofNullable(companyData.getDescription()).orElse(company.getDescription()));
            company.setAddress(Optional.ofNullable(companyData.getAddress()).orElse(company.getAddress()));
            company.setLogo(Optional.ofNullable(companyData.getLogo()).orElse(company.getLogo()));

            companyRepository.save(company);

            UpdateCompanyResponse resUpdateCompany = new UpdateCompanyResponse();
            BeanUtils.copyProperties(company, resUpdateCompany);

            return resUpdateCompany;
        } else {
            throw new ResourceNotFoundException("Company not found");
        }
    }

    public CreateCompanyResponse createCompany(CreateCompanyRequest companyData) {
        Company newCompany = Company.builder()
                .name(companyData.getName())
                .description(companyData.getDescription())
                .address(companyData.getAddress())
                .logo(companyData.getLogo())
                .build();
        companyRepository.save(newCompany);

        CreateCompanyResponse resCreateCompany = new CreateCompanyResponse();
        BeanUtils.copyProperties(newCompany, resCreateCompany);

        return resCreateCompany;
    }

    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null) {
            userRepository.deleteAllByCompany(company);
            jobRepository.deleteAllByCompany(company);
        }
        companyRepository.deleteById(id);
    }
}

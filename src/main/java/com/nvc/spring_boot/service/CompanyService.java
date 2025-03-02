package com.nvc.spring_boot.service;

import com.nvc.spring_boot.domain.Company;
import com.nvc.spring_boot.domain.response.PaginationDTO;
import com.nvc.spring_boot.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public PaginationDTO getList(Specification<Company> specification, Pageable pageable) {
        Page<Company> pageCompany = companyRepository.findAll(specification, pageable);
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

    public Company findCompany(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    public Company updateCompany(Company companyData) {
        Company company = findCompany(companyData.getId());
        if (company != null) {
            company.setName(companyData.getName());
            company.setDescription(companyData.getDescription());
            company.setAddress(companyData.getAddress());
            company.setLogo(companyData.getLogo());
            companyRepository.save(company);
        }

        return company;
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}

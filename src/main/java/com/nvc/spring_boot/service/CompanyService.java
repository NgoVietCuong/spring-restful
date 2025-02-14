package com.nvc.spring_boot.service;

import com.nvc.spring_boot.domain.Company;
import com.nvc.spring_boot.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getList() {
        return companyRepository.findAll();
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

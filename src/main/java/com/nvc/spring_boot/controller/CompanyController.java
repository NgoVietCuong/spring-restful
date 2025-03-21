package com.nvc.spring_boot.controller;

import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.dto.company.request.CreateCompanyRequest;
import com.nvc.spring_boot.dto.company.request.UpdateCompanyRequest;
import com.nvc.spring_boot.dto.company.response.CreateCompanyResponse;
import com.nvc.spring_boot.dto.company.response.FindCompanyResponse;
import com.nvc.spring_boot.dto.company.response.UpdateCompanyResponse;
import com.nvc.spring_boot.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nvc.spring_boot.entity.Company;
import com.nvc.spring_boot.service.CompanyService;


@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/list")
    @ApiMessage("get company list")
    public ResponseEntity<PaginationResponse> getList(
           @Filter Specification<Company> specification,
           Pageable pageable
    ) {
        return ResponseEntity.ok(companyService.getList(specification, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("get company info")
    public ResponseEntity<FindCompanyResponse> findCompany(@PathVariable("id") Long id) {
        return ResponseEntity.ok(companyService.findCompany(id));
    }

    @PutMapping()
    @ApiMessage("update company info")
    public ResponseEntity<UpdateCompanyResponse> updateCompany(@Valid @RequestBody UpdateCompanyRequest company) {
        return ResponseEntity.ok(companyService.updateCompany(company));
    }

    @PostMapping()
    @ApiMessage("create new company")
    public ResponseEntity<CreateCompanyResponse> createCompany(@Valid @RequestBody CreateCompanyRequest company) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(company));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete company")
    @Transactional
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

package com.nvc.spring_boot.controller;

import com.nvc.spring_boot.domain.response.PaginationDTO;
import com.nvc.spring_boot.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nvc.spring_boot.domain.Company;
import com.nvc.spring_boot.service.CompanyService;


@RestController
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies/list")
    @ApiMessage("get company list")
    public ResponseEntity<PaginationDTO> getList(
           @Filter Specification<Company> specification,
           Pageable pageable
    ) {
        return ResponseEntity.ok(companyService.getList(specification, pageable));
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("get company info")
    public ResponseEntity<Company> findCompany(@PathVariable("id") Long id) {
        return ResponseEntity.ok(companyService.findCompany(id));
    }

    @PutMapping("/companies")
    @ApiMessage("update company info")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.ok(companyService.updateCompany(company));
    }

    @PostMapping("/companies")
    @ApiMessage("create new company")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(company));
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("delete company")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok(null);
    }
}

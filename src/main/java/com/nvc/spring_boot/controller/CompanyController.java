package com.nvc.spring_boot.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nvc.spring_boot.domain.Company;
import com.nvc.spring_boot.service.CompanyService;
import com.nvc.spring_boot.util.error.IdInvalidException;


@RestController
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies/list")
    public ResponseEntity<List<Company>> getList(
            @RequestParam("page") Optional<String> pageOptional,
            @RequestParam("limit") Optional<String> limitOptional) {
        String page = pageOptional.isPresent() ? pageOptional.get() : "";
        String limit = limitOptional.isPresent() ? limitOptional.get() : "";
        return ResponseEntity.ok(companyService.getList());
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> findCompany(@PathVariable("id") Long id) {
        return ResponseEntity.ok(companyService.findCompany(id));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.ok(companyService.updateCompany(company));
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(company));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) throws IdInvalidException {
        if (id >= 100) {
            throw new IdInvalidException("Id must be less than 100");
        }
        companyService.deleteCompany(id);
        return ResponseEntity.ok(null);
    }
}

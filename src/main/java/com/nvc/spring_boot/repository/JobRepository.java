package com.nvc.spring_boot.repository;

import com.nvc.spring_boot.entity.Company;
import com.nvc.spring_boot.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    void deleteAllByCompany(Company company);
}

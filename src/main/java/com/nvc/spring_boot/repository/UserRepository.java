package com.nvc.spring_boot.repository;

import com.nvc.spring_boot.entity.Company;
import com.nvc.spring_boot.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);
    Boolean existsByEmail(String email);
    User findByEmailAndRefreshToken(String email, String token);
    void deleteAllByCompany(Company company);
}

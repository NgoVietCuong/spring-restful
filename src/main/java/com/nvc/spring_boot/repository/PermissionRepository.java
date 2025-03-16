package com.nvc.spring_boot.repository;

import com.nvc.spring_boot.entity.Permission;
import com.nvc.spring_boot.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    List<Permission> findPermissionsByIdIn(Collection<Long> ids);
    Boolean existsByMethodAndApiPathAndModuleEquals(String method, String apiPath, String module);
    Boolean existsByMethodAndApiPathAndModuleEqualsAndIdNot(String method, String apiPath, String module, Long id);
}

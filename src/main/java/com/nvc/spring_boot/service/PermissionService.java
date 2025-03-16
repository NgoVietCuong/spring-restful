package com.nvc.spring_boot.service;

import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.dto.permission.request.CreatePermissionRequest;
import com.nvc.spring_boot.dto.permission.request.UpdatePermissionRequest;
import com.nvc.spring_boot.dto.permission.response.CreatePermissionResponse;
import com.nvc.spring_boot.dto.permission.response.FindPermissionReponse;
import com.nvc.spring_boot.dto.permission.response.UpdatePermissionResponse;
import com.nvc.spring_boot.dto.skill.response.FindSkillResponse;
import com.nvc.spring_boot.entity.Permission;
import com.nvc.spring_boot.entity.Skill;
import com.nvc.spring_boot.repository.PermissionRepository;
import com.nvc.spring_boot.util.error.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public PaginationResponse getList(Specification<Permission> specification, Pageable pageable) {
        Page<Permission> pagePermission = permissionRepository.findAll(specification, pageable);
        PaginationResponse.PaginationMeta meta = PaginationResponse.PaginationMeta.builder()
                .currentPage(pageable.getPageNumber() + 1)
                .itemsPerPage(pageable.getPageSize())
                .totalPages(pagePermission.getTotalPages())
                .totalItems(pagePermission.getTotalElements())
                .build();

        List<FindPermissionReponse> resPermissions = pagePermission.getContent().stream().map(item -> {
            return FindPermissionReponse.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .apiPath(item.getApiPath())
                    .method(item.getMethod())
                    .module(item.getModule())
                    .createdAt(item.getCreatedAt())
                    .updatedAt(item.getUpdatedAt())
                    .build();
        }).toList();

        return PaginationResponse.builder()
                .meta(meta)
                .content(resPermissions)
                .build();
    }

    public FindPermissionReponse findPermission(Long id) {
        Permission permission = permissionRepository.findById(id).orElse(null);

        if (permission != null) {
            return FindPermissionReponse.builder()
                    .id(permission.getId())
                    .name(permission.getName())
                    .apiPath(permission.getApiPath())
                    .method(permission.getMethod())
                    .module(permission.getModule())
                    .createdAt(permission.getCreatedAt())
                    .updatedAt(permission.getUpdatedAt())
                    .build();
        }

        return null;
    }

    public CreatePermissionResponse createPermission(CreatePermissionRequest permissionData) {
        Boolean isPermissionExists = permissionRepository.existsByMethodAndApiPathAndModuleEquals(
                permissionData.getMethod(),
                permissionData.getApiPath(),
                permissionData.getModule()
        );
        if (isPermissionExists) throw new BadRequestException("Permission already exists");

        Permission newPermission = Permission.builder()
                .name(permissionData.getName())
                .method(permissionData.getMethod())
                .apiPath(permissionData.getApiPath())
                .module(permissionData.getModule())
                .build();

        permissionRepository.save(newPermission);

        return CreatePermissionResponse.builder()
                .id(newPermission.getId())
                .name(newPermission.getName())
                .apiPath(newPermission.getApiPath())
                .method(newPermission.getMethod())
                .module(newPermission.getModule())
                .createdAt(newPermission.getCreatedAt())
                .build();
    }

    public UpdatePermissionResponse updatePermission(UpdatePermissionRequest permissionData) {
        Permission permission = permissionRepository.findById(permissionData.getId())
                .orElseThrow(() -> new BadRequestException("Permission not found"));

        Boolean isPermissionExists = permissionRepository.existsByMethodAndApiPathAndModuleEqualsAndIdNot(
                permissionData.getMethod(),
                permissionData.getApiPath(),
                permissionData.getModule(),
                permissionData.getId()
        );

        if (isPermissionExists) throw new BadRequestException("Permission already exists");

        permission.setName(permissionData.getName());
        permission.setMethod(permissionData.getMethod());
        permission.setApiPath(permissionData.getApiPath());
        permission.setModule(permissionData.getModule());
        permissionRepository.save(permission);

        return UpdatePermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .apiPath(permission.getApiPath())
                .method(permission.getMethod())
                .module(permission.getModule())
                .updatedAt(permission.getUpdatedAt())
                .build();
    }

    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id).orElse(null);
        if (permission != null) {
            permission.getRoles().forEach(role -> role.getPermissions().remove(permission));
        }
        permissionRepository.deleteById(id);
    }
}

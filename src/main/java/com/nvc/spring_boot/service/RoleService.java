package com.nvc.spring_boot.service;

import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.dto.company.response.FindCompanyResponse;
import com.nvc.spring_boot.dto.permission.response.FindPermissionReponse;
import com.nvc.spring_boot.dto.role.request.CreateRoleRequest;
import com.nvc.spring_boot.dto.role.request.UpdateRoleRequest;
import com.nvc.spring_boot.dto.role.response.CreateRoleResponse;
import com.nvc.spring_boot.dto.role.response.FindRoleResponse;
import com.nvc.spring_boot.dto.role.response.UpdateRoleResponse;
import com.nvc.spring_boot.entity.Company;
import com.nvc.spring_boot.entity.Permission;
import com.nvc.spring_boot.entity.Role;
import com.nvc.spring_boot.repository.PermissionRepository;
import com.nvc.spring_boot.repository.RoleRepository;
import com.nvc.spring_boot.util.error.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public PaginationResponse getList(Specification<Role> specification, Pageable pageable) {
        Page<Role> pageRole = roleRepository.findAll(specification, pageable);
        PaginationResponse.PaginationMeta meta = PaginationResponse.PaginationMeta.builder()
                .currentPage(pageable.getPageNumber() + 1)
                .itemsPerPage(pageable.getPageSize())
                .totalPages(pageRole.getTotalPages())
                .totalItems(pageRole.getTotalElements())
                .build();

        List<FindRoleResponse> resRoles = pageRole.getContent().stream().map(role -> {
            List<FindRoleResponse.RolePermission> rolePermissions = role.getPermissions().stream().map(permission -> {
                return FindRoleResponse.RolePermission.builder()
                        .id(permission.getId())
                        .name(permission.getName())
                        .build();
            }).toList();

            return FindRoleResponse.builder()
                    .id(role.getId())
                    .name(role.getName())
                    .description(role.getDescription())
                    .isActive(role.getIsActive())
                    .permissions(rolePermissions)
                    .build();
        }).toList();

        return PaginationResponse.builder()
                .meta(meta)
                .content(resRoles)
                .build();
    }

    public FindRoleResponse findRole(Long id) {
        Role role = roleRepository.findById(id).orElse(null);

        if (role != null) {
            List<FindRoleResponse.RolePermission> rolePermissions = role.getPermissions().stream().map(permission -> {
                return FindRoleResponse.RolePermission.builder()
                        .id(permission.getId())
                        .name(permission.getName())
                        .build();
            }).toList();

            return FindRoleResponse.builder()
                    .id(role.getId())
                    .name(role.getName())
                    .description(role.getDescription())
                    .isActive(role.getIsActive())
                    .permissions(rolePermissions)
                    .build();
        }

        return null;
    }

    public CreateRoleResponse createRole(CreateRoleRequest roleData) {
        List<Permission> permissions = permissionRepository.findPermissionsByIdIn(roleData.getPermissions().stream().map(Permission::getId).toList());

        Role newRole = Role.builder()
                .name(roleData.getName())
                .description(roleData.getDescription())
                .isActive(roleData.getIsActive())
                .permissions(permissions)
                .build();

        roleRepository.save(newRole);

        List<CreateRoleResponse.RolePermission> rolePermissions = newRole.getPermissions().stream().map(permission -> {
            return CreateRoleResponse.RolePermission.builder()
                    .id(permission.getId())
                    .name(permission.getName())
                    .build();
        }).toList();

        return CreateRoleResponse.builder()
                .id(newRole.getId())
                .name(newRole.getName())
                .description(newRole.getDescription())
                .isActive(newRole.getIsActive())
                .permissions(rolePermissions)
                .build();
    }

    public UpdateRoleResponse updateRole(UpdateRoleRequest roleData) {
        Role role = roleRepository.findById(roleData.getId())
                .orElseThrow(() -> new BadRequestException("Role not found"));

        List<Permission> permissions = permissionRepository.findPermissionsByIdIn(roleData.getPermissions().stream().map(Permission::getId).toList());

        role.setName(roleData.getName());
        role.setDescription(roleData.getDescription());
        role.setIsActive(roleData.getIsActive());
        role.setPermissions(permissions);

        roleRepository.save(role);

        List<UpdateRoleResponse.RolePermission> rolePermissions = role.getPermissions().stream().map(permission -> {
            return UpdateRoleResponse.RolePermission.builder()
                    .id(permission.getId())
                    .name(permission.getName())
                    .build();
        }).toList();

        return UpdateRoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .isActive(role.getIsActive())
                .permissions(rolePermissions)
                .build();
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}

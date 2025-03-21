package com.nvc.spring_boot.controller;

import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.dto.role.request.CreateRoleRequest;
import com.nvc.spring_boot.dto.role.request.UpdateRoleRequest;
import com.nvc.spring_boot.dto.role.response.CreateRoleResponse;
import com.nvc.spring_boot.dto.role.response.FindRoleResponse;
import com.nvc.spring_boot.dto.role.response.UpdateRoleResponse;
import com.nvc.spring_boot.entity.Role;
import com.nvc.spring_boot.service.RoleService;
import com.nvc.spring_boot.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/list")
    @ApiMessage("get role list")
    public ResponseEntity<PaginationResponse> getList(
            @Filter Specification<Role> specification,
            Pageable pageable
    ) {
        return ResponseEntity.ok(roleService.getList(specification, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("get role info")
    public ResponseEntity<FindRoleResponse> findRole(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roleService.findRole(id));
    }

    @PostMapping()
    @ApiMessage("create new role")
    public ResponseEntity<CreateRoleResponse> createRole(@Valid @RequestBody CreateRoleRequest role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(role));
    }

    @PutMapping()
    @ApiMessage("update role info")
    public ResponseEntity<UpdateRoleResponse> updateRole(@Valid @RequestBody UpdateRoleRequest role) {
        return ResponseEntity.ok(roleService.updateRole(role));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete role")
    @Transactional
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

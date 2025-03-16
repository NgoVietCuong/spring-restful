package com.nvc.spring_boot.controller;

import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.dto.permission.request.CreatePermissionRequest;
import com.nvc.spring_boot.dto.permission.request.UpdatePermissionRequest;
import com.nvc.spring_boot.dto.permission.response.CreatePermissionResponse;
import com.nvc.spring_boot.dto.permission.response.FindPermissionReponse;
import com.nvc.spring_boot.dto.permission.response.UpdatePermissionResponse;
import com.nvc.spring_boot.entity.Permission;
import com.nvc.spring_boot.service.PermissionService;
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
@RequestMapping("/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/list")
    @ApiMessage("get permission list")
    public ResponseEntity<PaginationResponse> getList(
            @Filter Specification<Permission> specification,
            Pageable pageable) {
        return ResponseEntity.ok(permissionService.getList(specification, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("get permission info")
    public ResponseEntity<FindPermissionReponse> findPermission(@PathVariable("id") Long id) {
        return ResponseEntity.ok(permissionService.findPermission(id));
    }

    @PostMapping()
    @ApiMessage("create new permission")
    public ResponseEntity<CreatePermissionResponse> createPermission(@Valid @RequestBody CreatePermissionRequest permission) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.createPermission(permission));
    }

    @PutMapping()
    @ApiMessage("update permission")
    public ResponseEntity<UpdatePermissionResponse> createPermission(@Valid @RequestBody UpdatePermissionRequest permission) {
        return ResponseEntity.ok(permissionService.updatePermission(permission));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete permission")
    @Transactional
    public ResponseEntity<Void> deletePermission(@PathVariable("id") Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

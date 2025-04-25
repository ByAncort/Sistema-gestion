package com.app.authjwt.Controller;

import com.app.authjwt.Service.PermissionService;
import com.app.authjwt.dto.*;
import com.app.authjwt.Payload.request.RolPermissions;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions-management")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping("/permissions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PermissionDto> createPermission(@RequestParam String name) {
        return ResponseEntity.ok(permissionService.createPermission(name));
    }

    @PostMapping("/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoleDto> createRole(@RequestParam String name) {
        return ResponseEntity.ok(permissionService.createRole(name));
    }

    @PutMapping("/roles/add-for/permissions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoleDto> addPermissionsToRole(
            @RequestBody RolPermissions rolPermissions) {
        return ResponseEntity.ok(permissionService.addPermissionsToRole(rolPermissions));
    }

    @PutMapping("/users/{username}/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> addRoleToUser(
            @PathVariable String username,
            @RequestParam String roleName) {
        return ResponseEntity.ok(permissionService.addRoleToUser(username, roleName));
    }

    @GetMapping("/users/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> getUserPermissions(
            @PathVariable String username) {
        return ResponseEntity.ok(permissionService.getUserWithRolesAndPermissions(username));
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<PermissionDto>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(permissionService.getAllRoles());
    }
}

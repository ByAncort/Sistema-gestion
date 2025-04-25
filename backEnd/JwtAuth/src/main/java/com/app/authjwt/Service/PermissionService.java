package com.app.authjwt.Service;

import com.app.authjwt.Model.User.Permission;
import com.app.authjwt.Model.User.Role;
import com.app.authjwt.Model.User.User;
import com.app.authjwt.Repository.PermissionRepository;
import com.app.authjwt.Repository.RoleRepository;
import com.app.authjwt.Repository.UserRepository;
import com.app.authjwt.dto.PermissionDto;
import com.app.authjwt.dto.RoleDto;
import com.app.authjwt.dto.UserDto;
import com.app.authjwt.Payload.request.RolPermissions;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    // Gestion de roles y permisos
    @Transactional
    public PermissionDto createPermission(String permissionName) {
        Permission permission = Permission.builder()
                .name(permissionName)
                .build();
        permission = permissionRepository.save(permission);
        return mapToPermissionDto(permission);
    }

    @Transactional
    public RoleDto createRole(String roleName) {
        Role role = Role.builder()
                .name(roleName)
                .permissions(new HashSet<>())
                .build();
        role = roleRepository.save(role);
        return mapToRoleDto(role);
    }
    @Transactional
    public RoleDto addPermissionsToRole(RolPermissions rolPermissions) {
        Role role = roleRepository.findByName(rolPermissions.getRolName())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Set<Permission> permissions = rolPermissions.getPermissionNames().stream()
                .map(name -> permissionRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + name)))
                .collect(Collectors.toSet());

        role.getPermissions().addAll(permissions);
        role = roleRepository.save(role);
        return mapToRoleDto(role);
    }

    // Métodos para usuarios
    @Transactional
    public UserDto addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        user.getRoles().add(role);
        user = userRepository.save(user);
        return mapToUserDto(user);
    }

    @Transactional
    public UserDto removeRoleFromUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        user.getRoles().remove(role);
        user = userRepository.save(user);
        return mapToUserDto(user);
    }

    @Transactional
    public UserDto setUserRoles(String username, Set<String> roleNames) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Set<Role> roles = roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + name)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        user = userRepository.save(user);
        return mapToUserDto(user);
    }

    // Métodos de consulta
    public List<PermissionDto> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::mapToPermissionDto)
                .collect(Collectors.toList());
    }

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapToRoleDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserWithRolesAndPermissions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapToUserDto(user);
    }



    // Mappers
    private PermissionDto mapToPermissionDto(Permission permission) {
        return PermissionDto.builder()
                .name(permission.getName())
                .build();
    }

    private RoleDto mapToRoleDto(Role role) {
        return RoleDto.builder()
                .name(role.getName())
                .permissions(role.getPermissions().stream()
                        .map(this::mapToPermissionDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .usermane(user.getUsername())
                .Email(user.getEmail())
                .enabled(user.isEnabled())
                .roles(user.getRoles().stream()
                        .map(this::mapToRoleDto)
                        .collect(Collectors.toList()))
                .build();
    }
}

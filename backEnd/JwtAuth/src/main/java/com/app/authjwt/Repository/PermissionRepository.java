package com.app.authjwt.Repository;

import com.app.authjwt.Model.User.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByName(String roleName);

    Optional<Permission> findByName(String name);
}

package com.app.authjwt.Service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
public class VerifyService {
    public boolean hasAnyPermission(Authentication authentication, String... permissions) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        Arrays.stream(permissions)
                                .anyMatch(permission ->
                                        authority.getAuthority().equals(permission)));
    }

    public void verifyPermissions(String[] requiredPermissions,
                                  Authentication authentication,
                                  String errorMessage) {
        if (!this.hasAnyPermission(authentication, requiredPermissions)) {
            throw new AccessDeniedException(errorMessage);
        }
    }


}

package com.app.authjwt.Controller;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('ADMIN_PERMISSIONS') or hasAuthority(@security.getRequiredPermission())")
public @interface AdminOrPermission {
    String value();
}
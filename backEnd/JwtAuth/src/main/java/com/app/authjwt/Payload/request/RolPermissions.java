package com.app.authjwt.Payload.request;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RolPermissions {
    private String rolName;
    private Set<String> permissionNames;

}

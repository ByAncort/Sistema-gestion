package com.app.authjwt.Payload.response;

import com.app.authjwt.Model.User.Permission;
import com.app.authjwt.Model.User.Role;
import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseV2 {

    private String token;
    private String tokenType;
    private Instant issuedAt;
    private Instant expiresAt;
    private String username;
    private Set<Role> roles;
    private String refreshToken;
    private String message;

}

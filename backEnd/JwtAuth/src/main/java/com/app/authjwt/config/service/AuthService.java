package com.app.authjwt.config.service;

import com.app.authjwt.Model.User.Role;
import com.app.authjwt.Model.User.User;
import com.app.authjwt.Repository.RoleRepository;
import com.app.authjwt.Repository.UserRepository;
import com.app.authjwt.config.jwt.JwtUtils;
import com.app.authjwt.Payload.request.LoginRequest;
import com.app.authjwt.Payload.request.RegisterRequest;
import com.app.authjwt.Payload.response.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Date;


@Service

public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Autowired
    private  RoleRepository roleRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  JwtUtils jwtService;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Value("${auth.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public AuthResponse login(LoginRequest request) {
        try {
            // Single authentication point
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Get the authenticated user
            UserDetails user = (UserDetails) authentication.getPrincipal();

            Date issuedAt = new Date();
            Date expiration = new Date(System.currentTimeMillis() + jwtExpirationMs);
            String token = jwtService.getToken(user);

            return AuthResponse.builder()
                    .issuedAt(issuedAt)
                    .expiration(expiration)
                    .token(token)
                    .build();

        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password"
            );
        }
    }

    public AuthResponse register(RegisterRequest request) {

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        User user= User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(userRole))
                .build();

        userRepository.save(user);
        Date issuedAt = new Date();
        Date expiration = new Date(System.currentTimeMillis() + jwtExpirationMs);
        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .issuedAt(issuedAt)
                .expiration(expiration)
                .token(token)
                .build();
    }
}

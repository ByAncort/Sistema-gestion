package com.app.authjwt.Controller;

import com.app.authjwt.Repository.UserRepository;
import com.app.authjwt.config.service.AuthService;
import com.app.authjwt.Payload.request.LoginRequest;
import com.app.authjwt.Payload.request.RegisterRequest;
import com.app.authjwt.Payload.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  AuthService authService;



    @PostMapping(value = "signin")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "signup")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        return ResponseEntity.ok(authService.register(request));
    }
}

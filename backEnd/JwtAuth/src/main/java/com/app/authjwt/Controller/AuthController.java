package com.app.authjwt.Controller;

import com.app.authjwt.Payload.response.AuthResponseV2;
import com.app.authjwt.Repository.UserRepository;
import com.app.authjwt.config.service.AuthService;
import com.app.authjwt.Payload.request.LoginRequest;
import com.app.authjwt.Payload.request.RegisterRequest;
import com.app.authjwt.Payload.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  AuthService authService;



    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping(value = "register-v2")
    public ResponseEntity<AuthResponseV2> registerUser(@RequestBody RegisterRequest request) {
        AuthResponseV2 response = null;
        try {
            response = authService.createUser(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AuthResponseV2> handleException(RuntimeException e) {
        AuthResponseV2 errorResponse = AuthResponseV2.builder()
                .message(e.getMessage())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }
}

package com.takehome.stayease.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.takehome.stayease.dto.request.LoginRequest;
import com.takehome.stayease.dto.request.RegisterRequest;
import com.takehome.stayease.dto.response.AuthResponse;
import com.takehome.stayease.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(
        @RequestBody @Valid RegisterRequest registerRequest
    ) {
        log.info(
            "Request received to register new user with email '{}'", 
            registerRequest.getEmail()
        );
        return new ResponseEntity<>(
            authService.registerUser(registerRequest), 
            HttpStatus.OK
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        log.info("Request received to login user with email {}", loginRequest.getEmail());
        return new ResponseEntity<>(
            authService.loginUser(loginRequest),
            HttpStatus.OK
        );
    }
    
}

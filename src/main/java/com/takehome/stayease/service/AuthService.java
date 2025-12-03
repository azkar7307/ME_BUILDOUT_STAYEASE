package com.takehome.stayease.service;

import lombok.RequiredArgsConstructor;
import com.takehome.stayease.dto.request.LoginRequest;
import com.takehome.stayease.dto.request.RegisterRequest;
import com.takehome.stayease.dto.response.AuthResponse;
import com.takehome.stayease.entity.AppUser;
import com.takehome.stayease.repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AppUserRepository userRepository;
    private final ValidationService validationService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthResponse registerUser(RegisterRequest registerRequest) {
        validationService.ValidateUserExistByEmail(registerRequest.getEmail());
        AppUser user = modelMapper.map(registerRequest, AppUser.class);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        String jwtToken = jwtService.generateToken(user);
        userRepository.save(user);
        return new AuthResponse(true, jwtToken);
    }

    public AuthResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), 
                loginRequest.getPassword()
            )
        );
        AppUser user = (AppUser) authentication.getPrincipal();
        return new AuthResponse(true, jwtService.generateToken(user));
    }

    
}

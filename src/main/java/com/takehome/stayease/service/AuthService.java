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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AppUserRepository userRepository;
    private final ValidationService validationService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthResponse registerUser(RegisterRequest registerRequest) {
        validationService.ValidateUserExistByEmail(registerRequest.getEmail());
        AppUser user = modelMapper.map(registerRequest, AppUser.class);
        String jwtToken = jwtService.generateToken(user);
        userRepository.save(user);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse loginUser(LoginRequest loginRequest) {
        AppUser user = validationService.validateAndGetUserByEmail(loginRequest.getEmail());
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                user.getEmail(), 
                user.getPassword()
            )
        );
        return new AuthResponse(jwtService.generateToken(user));
    }

    
}

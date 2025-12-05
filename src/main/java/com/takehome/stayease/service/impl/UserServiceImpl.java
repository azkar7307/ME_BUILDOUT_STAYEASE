package com.takehome.stayease.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.takehome.stayease.exception.EntityNotFoundException;
import com.takehome.stayease.repository.AppUserRepository;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService {
    
    private final AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(
            () -> new EntityNotFoundException(
                    "User with email '" + username
                            + "' has not registered yet"
            )
    );
    }
}

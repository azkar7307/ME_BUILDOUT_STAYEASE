package com.takehome.stayease.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService {

  private final ValidationServiceImpl validationService;

  @Transactional(readOnly=true)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return validationService.validateAndGetUserByEmail(username);
  }
}

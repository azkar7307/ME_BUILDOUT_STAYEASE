package com.takehome.stayease.service;

import com.takehome.stayease.entity.AppUser;

public interface ValidationService {

    void ValidateUserExistByEmail(String email);

    AppUser validateAndGetUserByEmail(String email);
    
}

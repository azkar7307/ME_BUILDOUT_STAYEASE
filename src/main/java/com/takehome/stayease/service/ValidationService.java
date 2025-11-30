package com.takehome.stayease.service;

import com.takehome.stayease.entity.AppUser;
import com.takehome.stayease.entity.Hotel;

public interface ValidationService {

    void ValidateUserExistByEmail(String email);

    AppUser validateAndGetUserByEmail(String email);

    Hotel validateAndGetHotel(Long id);
    
}

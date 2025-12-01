package com.takehome.stayease.service;

import com.takehome.stayease.dto.request.BookingRequest;
import com.takehome.stayease.entity.AppUser;
import com.takehome.stayease.entity.Booking;
import com.takehome.stayease.entity.Hotel;

public interface ValidationService {

    void ValidateUserExistByEmail(String email);

    AppUser validateAndGetUserByEmail(String email);

    Hotel validateAndGetHotel(Long id);

    void validateBookingDates(BookingRequest bookingRequest);

    void validateRoomAvailablity(Hotel hotel);

    Booking validateUserAndBooking(Long bookingId, Long userId);

    Booking validateAndGetBooking(Long bookingId);

    
}

package com.takehome.stayease.service;

import com.takehome.stayease.dto.request.BookingRequest;
import com.takehome.stayease.dto.response.BookingResponse;
import com.takehome.stayease.entity.AppUser;

public interface BookingService {
    BookingResponse bookRoom(Long hotelId, BookingRequest bookingRequest, AppUser user);

    BookingResponse getBookingById(Long bookingId, AppUser user);

    void cancelBookingByid(Long bookingId);

    
}

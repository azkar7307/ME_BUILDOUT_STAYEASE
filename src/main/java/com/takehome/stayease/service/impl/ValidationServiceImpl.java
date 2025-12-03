package com.takehome.stayease.service.impl;

import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import com.takehome.stayease.dto.request.BookingRequest;
import com.takehome.stayease.entity.AppUser;
import com.takehome.stayease.entity.Booking;
import com.takehome.stayease.entity.Hotel;
import com.takehome.stayease.exception.BadRequestException;
import com.takehome.stayease.exception.EntityNotFoundException;
import com.takehome.stayease.repository.AppUserRepository;
import com.takehome.stayease.repository.BookingRepository;
import com.takehome.stayease.repository.HotelRepository;
import com.takehome.stayease.service.ValidationService;
import com.takehome.stayease.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    private final AppUserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;


    @Override
    @Transactional(readOnly = true)
    public void ValidateUserExistByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException(
                "User with email '" 
                    + Util.mask(email) 
                    + "' already exists"
            );
        }
        
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser validateAndGetUserByEmail(String email) {
        AppUser user = userRepository.findByEmail(email).orElseThrow(
            () -> new EntityNotFoundException(Util.mask(email), "User")
        );
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Hotel validateAndGetHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(id, "User")
        );
        return hotel;
    }

    @Override
    public void validateBookingDates(BookingRequest bookingRequest) {
        LocalDate currentDate = LocalDate.now();
        LocalDate checkInDate = bookingRequest.getCheckInDate();
        LocalDate checkOutDate = bookingRequest.getCheckOutDate();
        
        if (checkInDate.equals(currentDate) || checkInDate.isBefore(currentDate)) {
            throw new BadRequestException("Check-in-date must be future date");
        }

        if (checkInDate.isAfter(checkOutDate)) {
            throw new BadRequestException("Check-in-date must be bofore check-out-date");
        }
    }

    @Override
    public void validateRoomAvailablity(Hotel hotel) {
        if (hotel.getAvailableRooms() < 1) {
            throw new EntityNotFoundException("No rooms available in requested Hotel '" 
                + hotel.getId() + "'"
            );
        }
    }

    @Override
    public Booking validateUserAndBooking(Long bookingId, Long userId) {
        Booking booking = validateAndGetBooking(bookingId);
        if (!booking.getUser().getId().equals(userId)) {
            throw new BadRequestException("Customer '" 
                + userId 
                + " have not booked with Booking '" 
                + bookingId
                + "'"
            );
        }
        return booking;
    }

    @Override
    @Transactional(readOnly = true)
    public Booking validateAndGetBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
            () -> new EntityNotFoundException(bookingId, "Booking")
        );
        return booking;
    }
    
}

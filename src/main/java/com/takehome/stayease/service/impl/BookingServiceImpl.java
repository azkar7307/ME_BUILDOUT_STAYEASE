package com.takehome.stayease.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.takehome.stayease.dto.request.BookingRequest;
import com.takehome.stayease.dto.response.BookingResponse;
import com.takehome.stayease.entity.AppUser;
import com.takehome.stayease.entity.Booking;
import com.takehome.stayease.entity.Hotel;
import com.takehome.stayease.repository.BookingRepository;
import com.takehome.stayease.service.BookingService;
import com.takehome.stayease.service.ValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ValidationService validationService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingResponse bookRoom(Long hotelId, BookingRequest bookingRequest, AppUser user) {
        validationService.validateBookingDates(bookingRequest);
        Hotel hotel = validationService.validateAndGetHotel(hotelId);
        validationService.validateRoomAvailablity(hotel);
        Booking customerBooking = modelMapper.map(bookingRequest, Booking.class);
        customerBooking.setHotel(hotel);
        customerBooking.setUser(user);
        Booking savedCustomerBooking = bookingRepository.save(customerBooking);
        log.info("Customer '{}' booked a romm in Hotel '{}'", hotelId, user.getId());
        return modelMapper.map(savedCustomerBooking, BookingResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long bookingId, AppUser user) {
        Booking booking = validationService.validateUserAndBooking(bookingId, user.getId());
        log.info("Customer '{}'' fetched booking details", user.getId());
        return modelMapper.map(booking, BookingResponse.class);
    }

    @Override
    public void cancelBookingByid(Long bookingId) {
        Booking booking = validationService.validateAndGetBooking(bookingId);
        bookingRepository.delete(booking);
    }
    
}

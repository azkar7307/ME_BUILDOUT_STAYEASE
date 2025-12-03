package com.takehome.stayease.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.takehome.stayease.dto.request.BookingRequest;
import com.takehome.stayease.dto.response.BookingResponse;
import com.takehome.stayease.entity.AppUser;
import com.takehome.stayease.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/{hotelId}")
    ResponseEntity<BookingResponse> bookRoom(
        @PathVariable Long hotelId, 
        @RequestBody @Valid BookingRequest bookingRequest, 
        Authentication authentication
    ) {
        AppUser user = (AppUser) authentication.getPrincipal();
        log.info("Customer '{}' request to book a room in a hotel '{}'", user.getId(), hotelId);
        BookingResponse response = bookingService.bookRoom(hotelId, bookingRequest, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    ResponseEntity<BookingResponse> getBooingById(
        @PathVariable Long bookingId,
        Authentication authentication
    ) {
        AppUser user = (AppUser) authentication.getPrincipal();
        log.info(
            "Customer '{}' request to get booking details | bookingId '{}'", 
            user.getId(), 
            bookingId
        );
        BookingResponse response = bookingService.getBookingById(bookingId, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{bookingId}")
    ResponseEntity<Void> deleteBookingById(
        @PathVariable Long bookingId,
        Authentication authentication
    ) {
        AppUser user = (AppUser) authentication.getPrincipal();
        log.info("Customer request to concel booking '{}'", bookingId);
        bookingService.cancelBookingByid(bookingId);
        log.info(
            "Hotel_Manager '{}' cancelled the booking {} successfully", 
            user.getId(), 
            bookingId
        );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

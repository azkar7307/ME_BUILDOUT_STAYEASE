package com.takehome.stayease.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import com.takehome.stayease.dto.request.HotelRequest;
import com.takehome.stayease.dto.request.UpdateHotelRequest;
import com.takehome.stayease.dto.response.HotelResponse;
import com.takehome.stayease.dto.response.HotelsAvailableResponse;
import com.takehome.stayease.dto.response.UpdateHotelResponse;
import com.takehome.stayease.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelResponse> addHotel(
        @RequestBody @Valid HotelRequest hotelRequest
    ) {
        log.info("Request received to add new Hotel");
        HotelResponse hotelResponse = hotelService.addHotel(hotelRequest);
        return new ResponseEntity<>(hotelResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<HotelsAvailableResponse>> getAllAvailableHotels() {
        log.info("Request received to fetch all available Hotels");
        List<HotelsAvailableResponse> availableResponse = hotelService.getAllAvailableHotels();
        log.info("Fetched all available hotels successfully");
        return new ResponseEntity<>(availableResponse, HttpStatus.OK);
    }

    @PatchMapping("/{hotelId}")
    public ResponseEntity<UpdateHotelResponse> updateHotels(
        @PathVariable Long hotelId,
        @RequestBody @Valid UpdateHotelRequest updateRequest
    ) {
        log.info("Request received to update Hotel '{}'", hotelId);
        UpdateHotelResponse updateResponse = hotelService.updateHotels(hotelId, updateRequest);
        log.info("Hotel '{}' updated successfully", hotelId);
        return new ResponseEntity<>(updateResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> updateHotels(@PathVariable Long hotelId) {
        log.info("Request received to delete Hotel '{}'", hotelId);
        hotelService.deleteHotel(hotelId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}
}

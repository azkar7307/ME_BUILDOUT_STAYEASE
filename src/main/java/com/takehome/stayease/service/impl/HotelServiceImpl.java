package com.takehome.stayease.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import com.takehome.stayease.dto.request.HotelRequest;
import com.takehome.stayease.dto.request.UpdateHotelRequest;
import com.takehome.stayease.dto.response.HotelResponse;
import com.takehome.stayease.dto.response.HotelsAvailableResponse;
import com.takehome.stayease.dto.response.UpdateHotelResponse;
import com.takehome.stayease.entity.Hotel;
import com.takehome.stayease.repository.HotelRepository;
import com.takehome.stayease.service.HotelService;
import com.takehome.stayease.service.ValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final ValidationService ValidationService;
    private final ModelMapper modelMapper;

    @Override
    public HotelResponse addHotel(HotelRequest hotelRequest) {
        Hotel hotel = modelMapper.map(hotelRequest, Hotel.class);
        Hotel savedHotel = hotelRepository.save(hotel);
        log.info("Save new hotel '{}' successfully", savedHotel.getId());
        return modelMapper.map(savedHotel, HotelResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelsAvailableResponse> getAllAvailableHotels() {
        List<Hotel> availableHotels = hotelRepository.findAllAvailable();
        log.info("Fetched all available hotels from db successfully");
        return availableHotels.stream()
            .map(h -> modelMapper.map(h, HotelsAvailableResponse.class))
            .toList();
    }

    @Override
    @Transactional
    public UpdateHotelResponse updateHotel(Long id, UpdateHotelRequest updateRequest) {
        Hotel hotel = ValidationService.validateAndGetHotel(id);
        hotel.setName(updateRequest.getName());
        hotel.setAvailableRooms(updateRequest.getAvailableRooms());
        return modelMapper.map(hotel, UpdateHotelResponse.class);
    }

    @Override
    public void deleteHotel(Long id) {
        Hotel hotel = ValidationService.validateAndGetHotel(id);
        hotelRepository.delete(hotel);
        log.info("Hotel '{}' deleted successfully", id);
    }
    
}

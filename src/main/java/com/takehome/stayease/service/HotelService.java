package com.takehome.stayease.service;

import java.util.List;
import com.takehome.stayease.dto.request.HotelRequest;
import com.takehome.stayease.dto.request.UpdateHotelRequest;
import com.takehome.stayease.dto.response.HotelResponse;
import com.takehome.stayease.dto.response.HotelsAvailableResponse;
import com.takehome.stayease.dto.response.UpdateHotelResponse;

public interface HotelService {
    HotelResponse addHotel(HotelRequest hotelRequest);
    
    List<HotelsAvailableResponse> getAllAvailableHotels();

    UpdateHotelResponse updateHotels(Long id, UpdateHotelRequest updateRequest);

    void deleteHotel(Long id);
}

package com.takehome.stayease.config;

import com.takehome.stayease.dto.response.BookingResponse;
import com.takehome.stayease.entity.Booking;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper =  new ModelMapper();

        TypeMap<Booking, BookingResponse> bookingMap = modelMapper.createTypeMap(
            Booking.class, 
            BookingResponse.class
        );

        bookingMap.addMappings(mapper -> {
            mapper.map(Booking::getId, BookingResponse :: setBookingId);
            mapper.map(src -> src.getHotel().getId(), BookingResponse::setHotelId);
        }); 

        // bookingMap.addMappings(mapper -> {
        //     mapper.map(src -> src.getId(), (dest, value) -> dest.setBookingId((Long) value));
        //     mapper.map(src -> src.getHotel().getId(), (dest, value) -> dest.setHotelId((Long) value));
        // }); 
        return modelMapper;
    }
}

package com.takehome.stayease.modelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import com.takehome.stayease.config.AppConfig;
import com.takehome.stayease.dto.response.BookingResponse;
import com.takehome.stayease.entity.Booking;
import com.takehome.stayease.entity.Hotel;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class ModelMapperTest {

    ModelMapper modelMapper = new AppConfig().modelMapper();
    
    @Test
    void mapToBookResponse_From_Booking(){
        // arrange
        LocalDate currentDate = LocalDate.now();
        Hotel hotel = new Hotel();
        hotel.setId(5L);
        hotel.setName("new hotel");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCheckInDate(currentDate.plusDays(1));
        booking.setCheckOutDate(currentDate.plusDays(2));
        booking.setHotel(hotel);

        // execute
        BookingResponse bookingResponse = modelMapper.map(booking, BookingResponse.class);

        // assert
        assertEquals(booking.getId(), bookingResponse.getBookingId());
        assertEquals(booking.getHotel().getId(), bookingResponse.getHotelId());
        assertEquals(booking.getCheckInDate(), bookingResponse.getCheckInDate());
        assertEquals(booking.getCheckOutDate(), bookingResponse.getCheckOutDate());

    }
}

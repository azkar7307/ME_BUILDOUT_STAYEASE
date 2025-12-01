package com.takehome.stayease.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Optional;
import com.takehome.stayease.dto.request.BookingRequest;
import com.takehome.stayease.entity.AppUser;
import com.takehome.stayease.entity.Booking;
import com.takehome.stayease.entity.Hotel;
import com.takehome.stayease.exception.BadRequestException;
import com.takehome.stayease.repository.AppUserRepository;
import com.takehome.stayease.repository.BookingRepository;
import com.takehome.stayease.repository.HotelRepository;
import com.takehome.stayease.service.impl.ValidationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class ValidationServiceImplTest {
    
    @Mock
    AppUserRepository userRepository;

    @Mock
    HotelRepository hotelRepository;

    @Mock
    BookingRepository bookingRepository;

    @InjectMocks
    ValidationServiceImpl validationService;
    
// ######################### validateBookingDates() ###############################
    
    @Test
    void validateBookingDates_valid_return_void() {
        // arrange
        LocalDate currentDate = LocalDate.now();
        // When there're valid check in and check out dates, return void 
        LocalDate checkInDate = currentDate.plusDays(1);
        LocalDate checkOutDate = currentDate.plusDays(3);
        
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCheckInDate(checkInDate);
        bookingRequest.setCheckOutDate(checkOutDate);

        // execute
        validationService.validateBookingDates(bookingRequest);
    }

    @Test
    void validateBookingDates_When_CheckInDate_Is_CurrentDate_Throw_BadRequestException() {
        // arrange
        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(3);
        
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCheckInDate(checkInDate);
        bookingRequest.setCheckOutDate(checkOutDate);

        // assert and execute
        assertThrows(
            BadRequestException.class,
            () -> validationService.validateBookingDates(bookingRequest)
        );
    }

    @Test
    void validateBookingDates_When_CheckOutDate_Is_Before_CheckInDate_Throw_BadRequestException() {
        // arrange
        LocalDate currentDate = LocalDate.now();
        LocalDate checkInDate = currentDate.plusDays(5);
        LocalDate checkOutDate = currentDate.plusDays(1);
        
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCheckInDate(checkInDate);
        bookingRequest.setCheckOutDate(checkOutDate);

        // assert and execute
        assertThrows(
            BadRequestException.class,
            () -> validationService.validateBookingDates(bookingRequest)
        );
    }

// ######################### validateRoomAvailablity() ############################### 
    
    @Test
    void validateRoomAvailablity_Valid_Return_Void() {
        // arrange
        Hotel hotel = new Hotel();
        hotel.setAvailableRooms(3);

        // execute
        validationService.validateRoomAvailablity(hotel);
    }

    @Test
    void validateRoomAvailablity_Room_Not_Available_Throw_BadRequestException() {
        // arrange
        Hotel hotel = new Hotel();
        hotel.setAvailableRooms(0);

        // assert and execute
        assertThrows(BadRequestException.class,
            () -> validationService.validateRoomAvailablity(hotel)
        );
    }
// ######################### validateUserAndBooking() ############################### 
    @Test
    void validateUserAndBooking_Valid_Return_Nothing() {
        // arrange
        AppUser user = new AppUser();
        user.setId(1L);

        Booking booking = new Booking();
        booking.setUser(user);
        
        // setup
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        
        // execute
        validationService.validateUserAndBooking(1L, 1L);

        // verify
        verify(bookingRepository, times(1)).findById(anyLong());
        

    }

    @Test
    void validateUserAndBooking_Bookig_BelongsTo_Other_Customer_Throw_BadRequestException() {
        // arrange
        AppUser user = new AppUser();
        user.setId(562L);

        Booking booking = new Booking();
        booking.setUser(user);

        // setup
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        
        // execute
        assertThrows(BadRequestException.class,
            () -> validationService.validateUserAndBooking(1L, 1L)
        );

        // verify
        verify(bookingRepository, times(1)).findById(anyLong());   
    }
}

package com.takehome.stayease.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.takehome.stayease.dto.request.BookingRequest;
import com.takehome.stayease.dto.response.BookingResponse;
import com.takehome.stayease.entity.AppUser;
import com.takehome.stayease.entity.Booking;
import com.takehome.stayease.entity.Hotel;
import com.takehome.stayease.repository.BookingRepository;
import com.takehome.stayease.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
@MockitoSettings
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    
    @Mock 
    private ValidationService validationService;

    @Mock 
    private ModelMapper modelMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private AppUser user;
    private Hotel hotel;
    private Booking booking;
    private BookingRequest bookingRequest;
    
    @BeforeEach
    void setup() {
        // arrange
        user = new AppUser();

        booking = new Booking();

        hotel = new Hotel();
        
        bookingRequest = new BookingRequest();
    }

    @Test
    void bookRoom_Return_BookingResponse() {
        // arrange
        hotel.setAvailableRooms(10);
        // setup
        doNothing().when(validationService).validateBookingDates(any(BookingRequest.class));
        when(validationService.validateAndGetHotel(anyLong())).thenReturn(hotel);
        doNothing().when(validationService).validateRoomAvailablity(any(Hotel.class));

        when(modelMapper.map(any(BookingRequest.class), eq(Booking.class)))
            .thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(modelMapper.map(any(Booking.class), eq(BookingResponse.class)))
            .thenReturn(new BookingResponse());

        // execute
        BookingResponse response = bookingService.bookRoom(1L, bookingRequest, user);

        // assert
        assertNotNull(response);

        // verify
        verify(validationService, times(1)).validateBookingDates(any(BookingRequest.class));
        verify(validationService, times(1)).validateAndGetHotel(anyLong());
        verify(validationService, times(1)).validateRoomAvailablity(any(Hotel.class));

        verify(modelMapper, times(1)).map(any(BookingRequest.class), eq(Booking.class));
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(modelMapper, times(1)).map(any(Booking.class), eq(BookingResponse.class));
    }

    @Test
    void bookRoom_Verify_RoomAvailablityCount_Return_BookingResponse() {
        // arrange
        Integer availableRooms = 5;
        Long bookingId = 235L;
        Long hotelId = 324L;

        // setup
        doNothing().when(validationService).validateBookingDates(any(BookingRequest.class));

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0); 
            hotel.setId(id);
            hotel.setAvailableRooms(availableRooms);
            return hotel;
        }).when(validationService).validateAndGetHotel(anyLong());

        doNothing().when(validationService).validateRoomAvailablity(any(Hotel.class));

        doReturn(booking).when(modelMapper).map(any(BookingRequest.class), eq(Booking.class));
        
        doAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setId(bookingId);
            return booking;
        }).when(bookingRepository).save(any(Booking.class));
        
        doAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            BookingResponse bookingResponse = new BookingResponse();
            bookingResponse.setBookingId(booking.getId());
            // bookingResponse.setHotelId(booking.getHotel().getId());
            return bookingResponse;

        }).when(modelMapper).map((any(Booking.class)), eq(BookingResponse.class));
        
        
        // execute
        BookingResponse response = bookingService.bookRoom(hotelId, bookingRequest, user);
        
        // assert
        assertNotNull(response);
        assertEquals(bookingId, response.getBookingId());
        assertEquals(hotelId, hotel.getId());
        assertEquals(availableRooms -1, hotel.getAvailableRooms());

        // verify
        verify(validationService, times(1)).validateBookingDates(any(BookingRequest.class));
        verify(validationService, times(1)).validateAndGetHotel(anyLong());
        verify(validationService, times(1)).validateRoomAvailablity(any(Hotel.class));

        verify(modelMapper, times(1)).map(any(BookingRequest.class), eq(Booking.class));
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(modelMapper, times(1)).map(any(Booking.class), eq(BookingResponse.class));
    }

    @Test
    void getBookingById_Return_BookingResponse() {
        // arrage
        user.setId(1L);

        // setup
        when(validationService.validateUserAndBooking(anyLong(), anyLong())).thenReturn(booking);
        when(modelMapper.map(any(Booking.class), eq(BookingResponse.class)))
            .thenReturn(new BookingResponse());

        // execute 
        BookingResponse response = bookingService.getBookingById(1L, user);

        // assert
        assertNotNull(response);

        // verify
        verify(validationService, times(1)).validateUserAndBooking(anyLong(), anyLong());
        verify(modelMapper, times(1)).map(any(Booking.class), eq(BookingResponse.class));
    }

    @Test
    void cancelBookingByid_Return_void() {
        // setup
        when(validationService.validateAndGetBooking(anyLong())).thenReturn(booking);
        doNothing().when(bookingRepository).delete(any(Booking.class));

        // execute
        bookingService.cancelBookingByid(1L);

        // verify
        verify(validationService, times(1)).validateAndGetBooking(anyLong());
        verify(bookingRepository, times(1)).delete(any(Booking.class));
    }

    
}

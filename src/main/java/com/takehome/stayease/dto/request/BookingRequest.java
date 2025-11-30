package com.takehome.stayease.dto.request;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BookingRequest {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
}

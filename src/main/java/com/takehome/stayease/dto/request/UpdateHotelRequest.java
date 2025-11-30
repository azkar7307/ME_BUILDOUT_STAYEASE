package com.takehome.stayease.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHotelRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Number of rooms required")
    private Integer availableRooms;
}

package com.takehome.stayease.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelsAvailableResponse {
    private Long id;
    private String name;
    private String location;
    private String description; 
    private Integer availableRooms;
}

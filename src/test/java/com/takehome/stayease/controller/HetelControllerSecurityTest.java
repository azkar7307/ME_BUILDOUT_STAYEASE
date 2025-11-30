package com.takehome.stayease.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.takehome.stayease.dto.request.HotelRequest;
import com.takehome.stayease.dto.request.UpdateHotelRequest;
import com.takehome.stayease.dto.response.HotelResponse;
import com.takehome.stayease.dto.response.UpdateHotelResponse;
import com.takehome.stayease.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@Import(SecurityConfig.class)
@WebMvcTest(HotelController.class)
@AutoConfigureMockMvc(addFilters = true) // Make sure security filters are enabled
public class HetelControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    // @MockBean
    // private UserDetailsServiceImpl userDetailsService;  // mock dependency


    // ********************************* ADD HOTEL ****************************************************
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addHotel_With_ADMIN_Role_Return_Created() throws Exception {

        HotelResponse response = new HotelResponse();
        when(hotelService.addHotel(any(HotelRequest.class)))
               .thenReturn(response);

        mockMvc.perform(
            post("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Akash",
                            "Location": "Pune",
                            "totalRooms": 10
                        """)
        )
            .andExpect(status().isOk());
    
        verify(hotelService, times(1)).addHotel(any(HotelRequest.class));
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addHotel_With_ADMIN_Role_Name_Field_Empty() throws Exception {

        HotelResponse response = new HotelResponse();
        when(hotelService.addHotel(any(HotelRequest.class)))
               .thenReturn(response);

        mockMvc.perform(
            post("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "",
                            "Location": "Pune",
                            "totalRooms": 10
                        """)
        )
            .andExpect(status().isOk());
    
        verify(hotelService, times(1)).addHotel(any(HotelRequest.class));
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addHotel_With_ADMIN_Role_Location_Field_Null() throws Exception {

        HotelResponse response = new HotelResponse();
        when(hotelService.addHotel(any(HotelRequest.class)))
            .thenReturn(response);

        mockMvc.perform(
            post("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Sky Resort",
                            "Location": null,
                            "totalRooms": 10
                        """)
        )
            .andExpect(status().isOk());
    
        verify(hotelService, times(1)).addHotel(any(HotelRequest.class));
    }

    @Test
    @WithMockUser(roles = {"HOTEL_MANAGER"})
    void addHotel_With_HOTEL_MANAGER_Role_Return_Forbidden() throws Exception {

        // execute
        mockMvc.perform(
            post("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "",
                            "Location": "Pune",
                            "totalRooms": 10
                        """)
        )
            .andExpect(status().isForbidden());
        
        // verify
        verify(hotelService, never()).addHotel(any(HotelRequest.class));
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    void addHotel_With_CUSTOMER_Role_Return_Forbidden() throws Exception {

        mockMvc.perform(
            post("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "",
                            "Location": "Pune",
                            "totalRooms": 10
                        """)
        )
            .andExpect(status().isForbidden());
        
        // verify
        verify(hotelService, never()).addHotel(any(HotelRequest.class));
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    void addHotel_With_Unauthenticated_User_Return_Unauthorized() throws Exception {

        mockMvc.perform(
            post("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "",
                            "Location": "Pune",
                            "totalRooms": 10
                        """)
        )
            .andExpect(status().isUnauthorized());
        
        // verify
        verify(hotelService, never()).addHotel(any(HotelRequest.class));
    }

// ******************************************* UPDATE HOTEL ******************************************** 

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateHotel_With_HOTEL_MANAGER_Role_Return_Forbidden() throws Exception {

        mockMvc.perform(
            patch("/api/hotels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Sky Resort",
                            "availableRooms": 5
                        """)
        )
            .andExpect(status().isForbidden());
    
        verify(hotelService, never()).updateHotels(anyLong(), any(UpdateHotelRequest.class));
    }


    @Test
    @WithMockUser(roles = {"HOTEL_MANAGER"})
    void updateHotel_With_HOTEL_MANAGER_Role_Return_ok() throws Exception {

        UpdateHotelResponse response = new UpdateHotelResponse();
        when(hotelService.updateHotels(anyLong(), any(UpdateHotelRequest.class)))
               .thenReturn(response);

        mockMvc.perform(
            patch("/api/hotels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Sky Resort",
                            "availableRooms": 5
                        """)
        )
            .andExpect(status().isOk());
    
        verify(hotelService, times(1)).updateHotels(anyLong(), any(UpdateHotelRequest.class));
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    void updateHotel_With_HOTEL_CUSTOMER_Role_Return_Forbidden() throws Exception {

        mockMvc.perform(
            patch("/api/hotels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Sky Resort",
                            "availableRooms": 5
                        """)
        )
            .andExpect(status().isForbidden());
    
        verify(hotelService, never()).updateHotels(anyLong(), any(UpdateHotelRequest.class));
    }

    @Test
    void updateHotel_With_Unauthenticated_User_Return_Unauthorized() throws Exception {

        mockMvc.perform(
            patch("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Sky Resort",
                            "availableRooms": 5
                        """)
        )
            .andExpect(status().isUnauthorized());
    
        verify(hotelService, never()).updateHotels(anyLong(), any(UpdateHotelRequest.class));
    }

// ***************************** DELETE HOTEL ********************************* 


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteHotel_With_ADMIN_Role_Return_void() throws Exception {

        doNothing().when(hotelService).deleteHotel(anyLong());

        mockMvc.perform(
            delete("/api/hotels/1"))
            .andExpect(status().isOk());
    
        verify(hotelService, times(1)).deleteHotel(anyLong());
    }

    @Test
    @WithMockUser(roles = {"HOTEL_MANAGER"})
    void deleteHotel_With_HOTEL_MANAGER_Role_Return_Forbidden() throws Exception {

        mockMvc.perform(
            delete("/api/hotels/1"))
            .andExpect(status().isOk());
    
        verify(hotelService, never()).deleteHotel(anyLong());
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    void deleteHotel_With_CUSTOMER_Role_Return_Forbidden() throws Exception {

        mockMvc.perform(
            delete("/api/hotels/1"))
            .andExpect(status().isOk());
    
        verify(hotelService, never()).deleteHotel(anyLong());
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    void deleteHotel_With_Unauthenticated_User_Return_Unauthorized() throws Exception {

        mockMvc.perform(
            delete("/api/hotels"))
            .andExpect(status().isUnauthorized());
    
        verify(hotelService, never()).deleteHotel(anyLong());
    }

// ******************************************************************************************* 

}

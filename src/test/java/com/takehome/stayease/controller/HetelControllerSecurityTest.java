package com.takehome.stayease.controller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.IOException;
import com.takehome.stayease.config.JWTAuthenticationFilter;
import com.takehome.stayease.config.SecurityConfig;
import com.takehome.stayease.dto.request.HotelRequest;
import com.takehome.stayease.dto.request.UpdateHotelRequest;
import com.takehome.stayease.dto.response.HotelResponse;
import com.takehome.stayease.dto.response.UpdateHotelResponse;
import com.takehome.stayease.service.impl.HotelServiceImpl;
import com.takehome.stayease.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    
    @MockBean
    private HotelServiceImpl hotelService;

    @MockBean
    private UserServiceImpl userService; // Required by SecurityConfig


    @BeforeEach
    void setup() throws ServletException, IOException {
        doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }


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
                            "location": "Pune",
                            "totalRooms": 10
                        }
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
                            "location": "Pune",
                            "totalRooms": 10
                        }
                        """)
        )
            .andExpect(status().isBadRequest());
    
        verify(hotelService, never()).addHotel(any(HotelRequest.class));
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
                            "location": null,
                            "totalRooms": 10
                        }
                        """)
        )
            .andExpect(status().isBadRequest());
    
        verify(hotelService, never()).addHotel(any(HotelRequest.class));
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
                            "location": "Pune",
                            "totalRooms": 10
                        }
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
                            "location": "Pune",
                            "totalRooms": 10
                        }
                        """)
        )
            .andExpect(status().isForbidden());
        
        // verify
        verify(hotelService, never()).addHotel(any(HotelRequest.class));
    }

    @Test
    void addHotel_With_Unauthenticated_User_Return_Unauthorized() throws Exception {

        mockMvc.perform(
            post("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "gggdgf",
                            "location": "Pune",
                            "totalRooms": 10
                        }
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
            put("/api/hotels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Sky Resort",
                            "availableRooms": 5
                        }
                        """)
        )
            .andExpect(status().isForbidden());
    
        verify(hotelService, never()).updateHotel(anyLong(), any(UpdateHotelRequest.class));
    }


    @Test
    @WithMockUser(roles = {"HOTEL_MANAGER"})
    void updateHotel_With_HOTEL_MANAGER_Role_Return_ok() throws Exception {

        UpdateHotelResponse response = new UpdateHotelResponse();
        when(hotelService.updateHotel(anyLong(), any(UpdateHotelRequest.class)))
               .thenReturn(response);

        mockMvc.perform(
            put("/api/hotels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Sky Resort",
                            "availableRooms": 5
                        }
                        """)
        )
            .andExpect(status().isOk());
    
        verify(hotelService, times(1)).updateHotel(anyLong(), any(UpdateHotelRequest.class));
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    void updateHotel_With_HOTEL_CUSTOMER_Role_Return_Forbidden() throws Exception {

        mockMvc.perform(
            put("/api/hotels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Sky Resort",
                            "availableRooms": 5
                        }
                        """)
        )
            .andExpect(status().isForbidden());
    
        verify(hotelService, never()).updateHotel(anyLong(), any(UpdateHotelRequest.class));
    }

    @Test
    void updateHotel_With_Unauthenticated_User_Return_Unauthorized() throws Exception {

        mockMvc.perform(
            put("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Sky Resort",
                            "availableRooms": 5
                        }
                        """)
        )
            .andExpect(status().isUnauthorized());
    
        verify(hotelService, never()).updateHotel(anyLong(), any(UpdateHotelRequest.class));
    }

// ***************************** DELETE HOTEL ********************************* 


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteHotel_With_ADMIN_Role_Return_void() throws Exception {

        doNothing().when(hotelService).deleteHotel(anyLong());

        mockMvc.perform(
            delete("/api/hotels/1"))
            .andExpect(status().isNoContent());
    
        verify(hotelService, times(1)).deleteHotel(anyLong());
    }

    @Test
    @WithMockUser(roles = {"HOTEL_MANAGER"})
    void deleteHotel_With_HOTEL_MANAGER_Role_Return_Forbidden() throws Exception {

        mockMvc.perform(
            delete("/api/hotels/1"))
            .andExpect(status().isForbidden());
    
        verify(hotelService, never()).deleteHotel(anyLong());
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    void deleteHotel_With_CUSTOMER_Role_Return_Forbidden() throws Exception {

        mockMvc.perform(
            delete("/api/hotels/1"))
            .andExpect(status().isForbidden());
    
        verify(hotelService, never()).deleteHotel(anyLong());
    }

    @Test
    void deleteHotel_With_Unauthenticated_User_Return_Unauthorized() throws Exception {

        mockMvc.perform(
            delete("/api/hotels/1"))
            .andExpect(status().isUnauthorized());
    
        verify(hotelService, never()).deleteHotel(anyLong());
    }

// ******************************************************************************************* 

}

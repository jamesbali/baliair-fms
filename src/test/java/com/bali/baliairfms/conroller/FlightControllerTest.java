package com.bali.baliairfms.conroller;

import com.bali.baliairfms.dto.requestdto.FlightRequestDto;
import com.bali.baliairfms.dto.responsedto.FlightResponseDto;
import com.bali.baliairfms.security.jwt.JwtService;
import com.bali.baliairfms.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FlightController.class)
@AutoConfigureMockMvc(addFilters = false)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private FlightService flightService;
    @MockBean private JwtService jwtService;

    @WithMockUser(roles = "ADMIN")
    @Test
    void testCreateFlight() throws Exception {
        FlightRequestDto request = new FlightRequestDto(
                "FL123", 1L, Set.of(1L), "JFK", "LAX",
                LocalDateTime.now(), LocalDateTime.now().plusHours(5)
        );

        FlightResponseDto response = new FlightResponseDto(
                1L, "FL123", "JFK", "LAX",
                request.departureTime(), request.arrivalTime(),
                null, null
        );

        when(flightService.createFlight(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flightNumber").value("FL123"));
    }

    @Test
    void testGetFlightById() throws Exception {
        FlightResponseDto response = new FlightResponseDto(
                1L, "FL123", "JFK", "LAX",
                LocalDateTime.now(), LocalDateTime.now().plusHours(5),
                null, null
        );

        when(flightService.getFlightById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/flights/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.arrivalAirport").value("LAX"));
    }

    @Test
    void testGetAllFlights() throws Exception {
        FlightResponseDto response = new FlightResponseDto(
                1L, "FL123", "JFK", "LAX",
                LocalDateTime.now(), LocalDateTime.now().plusHours(5),
                null, null
        );

        when(flightService.getAllFlights()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].departureAirport").value("JFK"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testUpdateFlight() throws Exception {
        FlightRequestDto request = new FlightRequestDto(
                "FL123", 1L, Set.of(1L), "JFK", "LAX",
                LocalDateTime.now(), LocalDateTime.now().plusHours(5)
        );

        FlightResponseDto response = new FlightResponseDto(
                1L, "FL123", "JFK", "LAX",
                request.departureTime(), request.arrivalTime(),
                null, null
        );

        when(flightService.updateFlight(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/flights/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("FL123"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testDeleteFlight() throws Exception {
        doNothing().when(flightService).deleteFlight(1L);

        mockMvc.perform(delete("/api/v1/flights/1"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testDelayFlight() throws Exception {
        doNothing().when(flightService).markFlightAsDelayed(1L, "Weather");

        mockMvc.perform(patch("/api/v1/flights/1/delay?reason=Weather"))
                .andExpect(status().isNoContent());
    }
}

package com.bali.baliairfms.conroller;
import com.bali.baliairfms.dto.requestdto.AircraftRequestDto;
import com.bali.baliairfms.dto.responsedto.AircraftResponseDto;
import com.bali.baliairfms.security.jwt.JwtService;
import com.bali.baliairfms.service.AircraftService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.ArgumentMatchers.any;



@WebMvcTest(controllers = AircraftController.class)
@AutoConfigureMockMvc(addFilters = false)
class AircraftControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private AircraftService aircraftService;
    @MockBean private JwtService jwtService;

    @WithMockUser(roles = "ADMIN")
    @Test
    void testCreateAircraft() throws Exception {
        AircraftRequestDto request = new AircraftRequestDto("TAIL001", "Boeing 737", 180, 5500, "JFK");
        AircraftResponseDto response = new AircraftResponseDto(1L, "TAIL001", "Boeing 737", 180, 5500, "JFK", List.of());

        when(aircraftService.createAircraft(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/aircraft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testGetAircraftById() throws Exception {
        AircraftResponseDto response = new AircraftResponseDto(1L, "TAIL001", "Boeing 737", 180, 5500, "JFK", List.of());

        when(aircraftService.getAircraftById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/aircraft/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tailNumber").value("TAIL001"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testGetAllAircraft() throws Exception {
        AircraftResponseDto response = new AircraftResponseDto(1L, "TAIL001", "Boeing 737", 180, 5500, "JFK", List.of());

        when(aircraftService.getAllAircraft()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/aircraft"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tailNumber").value("TAIL001"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testUpdateAircraft() throws Exception {
        AircraftRequestDto request = new AircraftRequestDto("TAIL001", "Boeing 737", 180, 5500, "JFK");
        AircraftResponseDto response = new AircraftResponseDto(1L, "TAIL001", "Boeing 737", 180, 5500, "JFK", List.of());

        when(aircraftService.updateAircraft(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/aircraft/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Boeing 737"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testDeleteAircraft() throws Exception {
        doNothing().when(aircraftService).deleteAircraft(1L);

        mockMvc.perform(delete("/api/v1/aircraft/1"))
                .andExpect(status().isNoContent());
    }
}

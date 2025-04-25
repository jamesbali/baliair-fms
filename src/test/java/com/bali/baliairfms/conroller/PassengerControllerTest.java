package com.bali.baliairfms.conroller;

import com.bali.baliairfms.dto.requestdto.PassengerRequestDto;
import com.bali.baliairfms.dto.responsedto.PassengerResponseDto;
import com.bali.baliairfms.security.jwt.JwtService;
import com.bali.baliairfms.service.PassengerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PassengerController.class)
@AutoConfigureMockMvc(addFilters = false)
class PassengerControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private PassengerService passengerService;
    @MockBean
    private JwtService jwtService;

    private final PassengerRequestDto requestDto = new PassengerRequestDto(
            "jane.doe@baliair.com", "Jane", "Doe", "1234567890", "securePass"
    );

    private final PassengerResponseDto responseDto = new PassengerResponseDto(
            1L, "jane.doe@baliair.com", "Jane", "Doe", "1234567890", true
    );

    @Test
    void testSelfRegisterPassenger() throws Exception {
        when(passengerService.registerPassenger(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/passengers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("jane.doe@baliair.com"));
    }

    @WithMockUser(roles = "USER")
    @Test
    void testGetPassengerById() throws Exception {
        when(passengerService.getPassengerById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/passengers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testGetAllPassengers() throws Exception {
        when(passengerService.getAllPassengers()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/passengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUpdatePassenger() throws Exception {
        when(passengerService.updatePassenger(eq(1L), any())).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/passengers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }

    @WithMockUser(roles = "USER")
    @Test
    void testDeletePassenger() throws Exception {
        doNothing().when(passengerService).deletePassenger(1L);

        mockMvc.perform(delete("/api/v1/passengers/1"))
                .andExpect(status().isNoContent());
    }
}

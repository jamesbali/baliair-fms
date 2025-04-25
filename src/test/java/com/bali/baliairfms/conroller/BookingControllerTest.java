package com.bali.baliairfms.conroller;

import com.bali.baliairfms.dto.requestdto.BookingRequestDto;
import com.bali.baliairfms.dto.responsedto.BookingResponseDto;
import com.bali.baliairfms.model.enums.BookingStatus;
import com.bali.baliairfms.security.jwt.JwtService;
import com.bali.baliairfms.service.BookingService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc(addFilters = false) // disable JWT filter for testing
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;
    @MockBean private JwtService jwtService;

    @WithMockUser(roles = "USER")
    @Test
    void testCreateBooking() throws Exception {
        BookingRequestDto request = new BookingRequestDto(1L, 2L, "12A");
        BookingResponseDto response = new BookingResponseDto(
                99L, null, null, LocalDateTime.now(), BookingStatus.BOOKED, "12A"
        );

        when(bookingService.createBooking(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.seatNumber").value("12A"));
    }

    @WithMockUser(roles = "USER")
    @Test
    void testGetBookingById() throws Exception {
        BookingResponseDto response = new BookingResponseDto(
                99L, null, null, LocalDateTime.now(), BookingStatus.BOOKED, "12A"
        );

        when(bookingService.getBookingById(99L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/bookings/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("BOOKED"));
    }

    @WithMockUser(roles = "USER")
    @Test
    void testGetAllBookings() throws Exception {
        BookingResponseDto response = new BookingResponseDto(
                99L, null, null, LocalDateTime.now(), BookingStatus.BOOKED, "12A"
        );

        when(bookingService.getAllBookings()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatNumber").value("12A"));
    }

    @WithMockUser(roles = "USER")
    @Test
    void testGetBookingsByPassenger() throws Exception {
        BookingResponseDto response = new BookingResponseDto(
                99L, null, null, LocalDateTime.now(), BookingStatus.BOOKED, "12A"
        );

        when(bookingService.getBookingsByPassenger(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/bookings/passenger/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatNumber").value("12A"));
    }

    @WithMockUser(roles = "USER")
    @Test
    void testGetBookingsByFlight() throws Exception {
        BookingResponseDto response = new BookingResponseDto(
                99L, null, null, LocalDateTime.now(), BookingStatus.BOOKED, "12A"
        );

        when(bookingService.getBookingsByFlight(2L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/bookings/flight/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatNumber").value("12A"));
    }

    @WithMockUser(roles = "USER")
    @Test
    void testCancelBooking() throws Exception {
        doNothing().when(bookingService).cancelBooking(99L);

        mockMvc.perform(patch("/api/v1/bookings/99/cancel"))
                .andExpect(status().isNoContent());
    }
}

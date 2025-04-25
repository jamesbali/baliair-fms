package com.bali.baliairfms.conroller;


import com.bali.baliairfms.dto.requestdto.BookingRequestDto;
import com.bali.baliairfms.dto.responsedto.BookingResponseDto;
import com.bali.baliairfms.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // Create a booking
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingRequestDto dto) {
        BookingResponseDto created = bookingService.createBooking(dto);
        return ResponseEntity.created(URI.create("/api/v1/bookings/" + created.id())).body(created);
    }

    // Get booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    // List all bookings
    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // List bookings by passenger
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByPassenger(@PathVariable Long passengerId) {
        return ResponseEntity.ok(bookingService.getBookingsByPassenger(passengerId));
    }

    // List bookings by flight
    @GetMapping("/flight/{flightId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByFlight(@PathVariable Long flightId) {
        return ResponseEntity.ok(bookingService.getBookingsByFlight(flightId));
    }

    // Cancel booking
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}

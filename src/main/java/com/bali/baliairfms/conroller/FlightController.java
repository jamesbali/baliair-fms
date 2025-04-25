package com.bali.baliairfms.conroller;

import com.bali.baliairfms.dto.requestdto.FlightRequestDto;
import com.bali.baliairfms.dto.requestdto.FlightSearchDto;
import com.bali.baliairfms.dto.responsedto.FlightResponseDto;
import com.bali.baliairfms.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    //Create a flight
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<FlightResponseDto> createFlight(@Valid @RequestBody FlightRequestDto dto) {
        FlightResponseDto created = flightService.createFlight(dto);
        return ResponseEntity.created(URI.create("/api/v1/flights/" + created.id())).body(created);
    }

    // Get flight by ID
    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDto> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    // Get all flights
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<FlightResponseDto>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    // Update a flight
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<FlightResponseDto> updateFlight(@PathVariable Long id,
                                                          @Valid @RequestBody FlightRequestDto dto) {
        return ResponseEntity.ok(flightService.updateFlight(id, dto));
    }

    // Delete a flight
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
    // Delay Flight
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/delay")
    public ResponseEntity<Void> delayFlight(@PathVariable Long id,
                                            @RequestParam String reason) {
        flightService.markFlightAsDelayed(id, reason);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/search")
    public ResponseEntity<Page<FlightResponseDto>> searchFlights(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String departureAirport,
            @RequestParam(required = false) String arrivalAirport,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        FlightSearchDto searchDto = new FlightSearchDto(flightNumber, departureAirport, arrivalAirport, departureDate);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(flightService.searchFlights(searchDto, pageable));
    }


}








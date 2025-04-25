
package com.bali.baliairfms.dto.requestdto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

public record FlightRequestDto(

        @NotBlank(message = "Flight number is required")
        String flightNumber,

        @NotNull(message = "Aircraft ID is required")
        Long aircraftId,

        @NotEmpty(message = "At least one crew member ID must be assigned")
        Set<Long> crewMemberIds,

        @NotBlank(message = "Departure airport is required")
        String departureAirport,

        @NotBlank(message = "Arrival airport is required")
        String arrivalAirport,

        @NotNull(message = "Departure time is required")
        LocalDateTime departureTime,

        @NotNull(message = "Arrival time is required")
        LocalDateTime arrivalTime
) {}


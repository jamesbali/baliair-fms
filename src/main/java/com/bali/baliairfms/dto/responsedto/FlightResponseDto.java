package com.bali.baliairfms.dto.responsedto;

import java.time.LocalDateTime;
import java.util.Set;

public record FlightResponseDto(
        Long id,
        String flightNumber,
        String departureAirport,
        String arrivalAirport,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        AircraftResponseDto aircraft,
        Set<CrewMemberResponseDto> crewMembers
) {
}

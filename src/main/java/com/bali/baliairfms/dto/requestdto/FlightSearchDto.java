package com.bali.baliairfms.dto.requestdto;

import java.time.LocalDate;

public record FlightSearchDto(

        String flightNumber,
        String departureAirport,
        String arrivalAirport,
        LocalDate departureDate

) {
}

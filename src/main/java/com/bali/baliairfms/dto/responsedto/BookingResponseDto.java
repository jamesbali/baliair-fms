package com.bali.baliairfms.dto.responsedto;

import com.bali.baliairfms.model.enums.BookingStatus;

import java.time.LocalDateTime;

public record BookingResponseDto (

        Long id,
        PassengerResponseDto passenger,
        FlightResponseDto flight,
        LocalDateTime bookingTime,
        BookingStatus status,
        String seatNumber
) {
}

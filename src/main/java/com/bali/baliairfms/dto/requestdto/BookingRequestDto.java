package com.bali.baliairfms.dto.requestdto;
import jakarta.validation.constraints.*;

public record BookingRequestDto(

        @NotNull(message = "Passenger ID is required")
        Long passengerId,

        @NotNull(message = "Flight ID is required")
        Long flightId,

        String seatNumber
) {
}

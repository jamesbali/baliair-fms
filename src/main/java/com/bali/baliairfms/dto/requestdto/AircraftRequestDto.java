package com.bali.baliairfms.dto.requestdto;

import jakarta.validation.constraints.*;

public record AircraftRequestDto(

        @NotBlank(message = "Aircraft tail number is required")
        String tailNumber,

        @NotBlank(message = "Aircraft model is required")
        String model,

        @Min(value = 1, message = "Seating capacity must be greater than 0")
        int seatingCapacity,

        @Min(value = 1, message = "Range (in km) must be greater than 0")
        int rangeKm,

        @NotBlank(message = "Current location is required")
        String currentLocation

) {
}

package com.bali.baliairfms.dto.responsedto;

public record PassengerResponseDto(

        Long id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        boolean active
) {
}

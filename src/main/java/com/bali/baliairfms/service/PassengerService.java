package com.bali.baliairfms.service;

import com.bali.baliairfms.dto.requestdto.PassengerRequestDto;
import com.bali.baliairfms.dto.responsedto.PassengerResponseDto;

import java.util.List;

public interface PassengerService {

    PassengerResponseDto registerPassenger(PassengerRequestDto dto);

    PassengerResponseDto getPassengerById(Long id);

    List<PassengerResponseDto> getAllPassengers();

    PassengerResponseDto updatePassenger(Long id, PassengerRequestDto dto);

    void deletePassenger(Long id);
}

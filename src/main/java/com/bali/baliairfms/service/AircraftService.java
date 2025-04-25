package com.bali.baliairfms.service;

import com.bali.baliairfms.dto.requestdto.AircraftRequestDto;
import com.bali.baliairfms.dto.responsedto.AircraftResponseDto;

import java.util.List;

public interface AircraftService {

    AircraftResponseDto createAircraft(AircraftRequestDto dto);

    AircraftResponseDto getAircraftById(Long id);

    List<AircraftResponseDto> getAllAircraft();

    AircraftResponseDto updateAircraft(Long id, AircraftRequestDto dto);

    void deleteAircraft(Long id);
}

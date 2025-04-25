package com.bali.baliairfms.service;

import com.bali.baliairfms.dto.requestdto.FlightRequestDto;
import com.bali.baliairfms.dto.responsedto.FlightResponseDto;

import java.util.List;

public interface FlightService {

    FlightResponseDto createFlight(FlightRequestDto dto);

    FlightResponseDto getFlightById(Long id);

    List<FlightResponseDto> getAllFlights();

    FlightResponseDto updateFlight(Long id, FlightRequestDto dto);

    void deleteFlight(Long id);

    void markFlightAsDelayed(Long flightId, String reason);

}

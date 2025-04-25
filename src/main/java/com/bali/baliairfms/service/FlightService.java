package com.bali.baliairfms.service;

import com.bali.baliairfms.dto.requestdto.FlightRequestDto;
import com.bali.baliairfms.dto.requestdto.FlightSearchDto;
import com.bali.baliairfms.dto.responsedto.FlightResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FlightService {

    FlightResponseDto createFlight(FlightRequestDto dto);

    FlightResponseDto getFlightById(Long id);

    List<FlightResponseDto> getAllFlights();

    FlightResponseDto updateFlight(Long id, FlightRequestDto dto);

    void deleteFlight(Long id);

    void markFlightAsDelayed(Long flightId, String reason);

    Page<FlightResponseDto> searchFlights(FlightSearchDto searchDto, Pageable pageable);



}

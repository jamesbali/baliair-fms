package com.bali.baliairfms.service.impl;

import com.bali.baliairfms.dto.requestdto.AircraftRequestDto;
import com.bali.baliairfms.dto.responsedto.AircraftResponseDto;
import com.bali.baliairfms.exception.ResourceNotFoundException;
import com.bali.baliairfms.mapper.AircraftMapper;
import com.bali.baliairfms.model.Aircraft;
import com.bali.baliairfms.repository.AircraftRepository;
import com.bali.baliairfms.service.AircraftService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AircraftServiceImpl implements AircraftService {

    private final AircraftRepository aircraftRepository;
    private final AircraftMapper aircraftMapper;

    @Override
    @Transactional
    public AircraftResponseDto createAircraft(AircraftRequestDto dto) {
        Aircraft aircraft = aircraftMapper.toEntity(dto);
        return aircraftMapper.toDto(aircraftRepository.save(aircraft));
    }

    @Override
    public AircraftResponseDto getAircraftById(Long id) {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + id));
        return aircraftMapper.toDto(aircraft);
    }

    @Override
    public List<AircraftResponseDto> getAllAircraft() {
        return aircraftRepository.findAll()
                .stream()
                .map(aircraftMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AircraftResponseDto updateAircraft(Long id, AircraftRequestDto dto) {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + id));
        aircraftMapper.updateAircraftFromDto(dto, aircraft);
        return aircraftMapper.toDto(aircraftRepository.save(aircraft));
    }

    @Override
    @Transactional
    public void deleteAircraft(Long id) {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + id));
        aircraftRepository.delete(aircraft);
    }
}

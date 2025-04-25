package com.bali.baliairfms.service.impl;

import com.bali.baliairfms.dto.requestdto.PassengerRequestDto;
import com.bali.baliairfms.dto.responsedto.PassengerResponseDto;
import com.bali.baliairfms.exception.DuplicateResourceException;
import com.bali.baliairfms.exception.ResourceNotFoundException;
import com.bali.baliairfms.mapper.PassengerMapper;
import com.bali.baliairfms.model.Passenger;
import com.bali.baliairfms.repository.PassengerRepository;
import com.bali.baliairfms.service.PassengerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override
    @Transactional
    public PassengerResponseDto registerPassenger(PassengerRequestDto dto) {
        if (passengerRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Email already registered: " + dto.email());
        }

        Passenger passenger = passengerMapper.toEntity(dto);
        return passengerMapper.toDto(passengerRepository.save(passenger));
    }

    @Override
    public PassengerResponseDto getPassengerById(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with id: " + id));
        return passengerMapper.toDto(passenger);
    }

    @Override
    public List<PassengerResponseDto> getAllPassengers() {
        return passengerRepository.findAll().stream()
                .map(passengerMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public PassengerResponseDto updatePassenger(Long id, PassengerRequestDto dto) {
        Passenger existing = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with id: " + id));
        passengerMapper.updatePassengerFromDto(dto, existing);
        return passengerMapper.toDto(passengerRepository.save(existing));
    }

    @Override
    @Transactional
    public void deletePassenger(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with id: " + id));
        passengerRepository.delete(passenger);
    }

    
}

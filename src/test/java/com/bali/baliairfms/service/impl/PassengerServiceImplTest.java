package com.bali.baliairfms.service.impl;

import com.bali.baliairfms.dto.requestdto.PassengerRequestDto;
import com.bali.baliairfms.dto.responsedto.PassengerResponseDto;
import com.bali.baliairfms.exception.DuplicateResourceException;
import com.bali.baliairfms.exception.ResourceNotFoundException;
import com.bali.baliairfms.mapper.PassengerMapper;
import com.bali.baliairfms.model.Passenger;
import com.bali.baliairfms.repository.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PassengerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerMapper passengerMapper;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    private Passenger passenger;
    private PassengerRequestDto requestDto;
    private PassengerResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("john.doe@baliair.com");

        requestDto = new PassengerRequestDto(
                "james.dan@baliair.com",
                "James",
                "Dan",
                "1234567890",       // Valid phone number
                "securePass123"     // Valid password (6+ characters)
        );

        responseDto = new PassengerResponseDto(
                1L,
                "james.dan@baliair.com",
                "James",
                "Dan",
                "1234567890",
                true
        );
    }

    @Test
    void testRegisterPassenger_success() {
        when(passengerRepository.existsByEmail("james.dan@baliair.com")).thenReturn(false);
        when(passengerMapper.toEntity(requestDto)).thenReturn(passenger);
        when(passengerRepository.save(passenger)).thenReturn(passenger);
        when(passengerMapper.toDto(passenger)).thenReturn(responseDto);

        PassengerResponseDto result = passengerService.registerPassenger(requestDto);
        assertEquals(responseDto, result);
    }

    @Test
    void testRegisterPassenger_duplicateEmail() {
        when(passengerRepository.existsByEmail("james.dan@baliair.com")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> passengerService.registerPassenger(requestDto));
    }

    @Test
    void testGetPassengerById_found() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(passengerMapper.toDto(passenger)).thenReturn(responseDto);

        PassengerResponseDto result = passengerService.getPassengerById(1L);
        assertEquals(responseDto, result);
    }

    @Test
    void testGetPassengerById_notFound() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> passengerService.getPassengerById(1L));
    }

    @Test
    void testGetAllPassengers() {
        when(passengerRepository.findAll()).thenReturn(List.of(passenger));
        when(passengerMapper.toDto(passenger)).thenReturn(responseDto);

        List<PassengerResponseDto> result = passengerService.getAllPassengers();
        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
    }

    @Test
    void testUpdatePassenger_success() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        doNothing().when(passengerMapper).updatePassengerFromDto(requestDto, passenger);
        when(passengerRepository.save(passenger)).thenReturn(passenger);
        when(passengerMapper.toDto(passenger)).thenReturn(responseDto);

        PassengerResponseDto result = passengerService.updatePassenger(1L, requestDto);
        assertEquals(responseDto, result);
    }

    @Test
    void testUpdatePassenger_notFound() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> passengerService.updatePassenger(1L, requestDto));
    }

    @Test
    void testDeletePassenger_success() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        doNothing().when(passengerRepository).delete(passenger);

        assertDoesNotThrow(() -> passengerService.deletePassenger(1L));
        verify(passengerRepository).delete(passenger);
    }

    @Test
    void testDeletePassenger_notFound() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> passengerService.deletePassenger(1L));
    }

}
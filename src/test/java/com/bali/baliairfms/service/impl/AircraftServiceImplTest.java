package com.bali.baliairfms.service.impl;

import com.bali.baliairfms.dto.requestdto.AircraftRequestDto;
import com.bali.baliairfms.dto.responsedto.AircraftResponseDto;
import com.bali.baliairfms.exception.ResourceNotFoundException;
import com.bali.baliairfms.mapper.AircraftMapper;
import com.bali.baliairfms.model.Aircraft;
import com.bali.baliairfms.repository.AircraftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AircraftServiceImplTest {

    @Mock
    private AircraftRepository aircraftRepository;

    @Mock
    private AircraftMapper aircraftMapper;

    @InjectMocks
    private AircraftServiceImpl aircraftService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAircraft_shouldReturnSavedAircraft() {
        AircraftRequestDto requestDto = new AircraftRequestDto("N737", "Boeing 737", 180, 3500, "JFK");
        Aircraft aircraftEntity = new Aircraft();
        AircraftResponseDto responseDto = new AircraftResponseDto(1L, "N737", "Boeing 737", 180, 3500, "JFK", null);

        when(aircraftMapper.toEntity(requestDto)).thenReturn(aircraftEntity);
        when(aircraftRepository.save(aircraftEntity)).thenReturn(aircraftEntity);
        when(aircraftMapper.toDto(aircraftEntity)).thenReturn(responseDto);

        AircraftResponseDto result = aircraftService.createAircraft(requestDto);

        assertEquals(responseDto, result);
        verify(aircraftRepository).save(aircraftEntity);
    }

    @Test
    void getAircraftById_shouldReturnAircraft_ifExists() {
        Long id = 1L;
        Aircraft aircraft = new Aircraft();
        AircraftResponseDto responseDto = new AircraftResponseDto(1L, "N737", "Boeing 737", 180, 3500, "JFK", null);

        when(aircraftRepository.findById(id)).thenReturn(Optional.of(aircraft));
        when(aircraftMapper.toDto(aircraft)).thenReturn(responseDto);

        AircraftResponseDto result = aircraftService.getAircraftById(id);

        assertEquals(responseDto, result);
        verify(aircraftRepository).findById(id);
    }

    @Test
    void getAircraftById_shouldThrowException_ifNotFound() {
        Long id = 1L;

        when(aircraftRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> aircraftService.getAircraftById(id));
    }

    @Test
    void getAllAircraft_shouldReturnAllAircraftDtos() {
        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);
        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(2L);

        List<Aircraft> aircraftList = Arrays.asList(aircraft1, aircraft2);

        AircraftResponseDto dto1 = new AircraftResponseDto(1L, "N737", "Boeing 737", 180, 3500, "JFK", null);
        AircraftResponseDto dto2 = new AircraftResponseDto(2L, "N738", "Airbus A320", 160, 3000, "LAX", null);
        List<AircraftResponseDto> expectedDtos = Arrays.asList(dto1, dto2);

        when(aircraftRepository.findAll()).thenReturn(aircraftList);
        when(aircraftMapper.toDto(aircraft1)).thenReturn(dto1);
        when(aircraftMapper.toDto(aircraft2)).thenReturn(dto2);

        List<AircraftResponseDto> result = aircraftService.getAllAircraft();

        assertEquals(expectedDtos, result);
        verify(aircraftRepository).findAll();
    }


    @Test
    void updateAircraft_shouldUpdateAndReturnDto() {
        Long id = 1L;
        AircraftRequestDto requestDto = new AircraftRequestDto("N737", "Boeing 737", 180, 3500, "JFK");
        Aircraft existingAircraft = new Aircraft();
        AircraftResponseDto updatedDto = new AircraftResponseDto(1L, "N737", "Boeing 737", 180, 3500, "JFK", null);

        when(aircraftRepository.findById(id)).thenReturn(Optional.of(existingAircraft));
        doNothing().when(aircraftMapper).updateAircraftFromDto(requestDto, existingAircraft);
        when(aircraftRepository.save(existingAircraft)).thenReturn(existingAircraft);
        when(aircraftMapper.toDto(existingAircraft)).thenReturn(updatedDto);

        AircraftResponseDto result = aircraftService.updateAircraft(id, requestDto);

        assertEquals(updatedDto, result);
        verify(aircraftRepository).save(existingAircraft);
    }

    @Test
    void deleteAircraft_shouldDeleteIfExists() {
        Long id = 1L;
        Aircraft aircraft = new Aircraft();

        when(aircraftRepository.findById(id)).thenReturn(Optional.of(aircraft));

        aircraftService.deleteAircraft(id);

        verify(aircraftRepository).delete(aircraft);
    }

    @Test
    void deleteAircraft_shouldThrowException_ifNotFound() {
        Long id = 1L;

        when(aircraftRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> aircraftService.deleteAircraft(id));
    }

}
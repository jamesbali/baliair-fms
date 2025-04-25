package com.bali.baliairfms.service.impl;

import com.bali.baliairfms.dto.requestdto.FlightRequestDto;
import com.bali.baliairfms.dto.requestdto.NotificationRequest;
import com.bali.baliairfms.dto.responsedto.FlightResponseDto;
import com.bali.baliairfms.exception.DuplicateResourceException;
import com.bali.baliairfms.exception.ResourceNotFoundException;
import com.bali.baliairfms.mapper.FlightMapper;
import com.bali.baliairfms.model.*;
import com.bali.baliairfms.repository.AircraftRepository;
import com.bali.baliairfms.repository.BookingRepository;
import com.bali.baliairfms.repository.CrewMemberRepository;
import com.bali.baliairfms.repository.FlightRepository;
import com.bali.baliairfms.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightServiceImplTest {

    @Mock
    private FlightRepository flightRepository;
    @Mock private AircraftRepository aircraftRepository;
    @Mock private CrewMemberRepository crewMemberRepository;
    @Mock private BookingRepository bookingRepository;
    @Mock private FlightMapper flightMapper;
    @Mock private NotificationService notificationService;

    @InjectMocks
    private FlightServiceImpl flightService;

    private Flight flight;
    private Aircraft aircraft;
    private CrewMember crewMember;
    private FlightRequestDto requestDto;
    private FlightResponseDto responseDto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        aircraft = new Aircraft();
        aircraft.setId(1L);

        crewMember = new CrewMember();
        crewMember.setId(10L);

        flight = Flight.builder()
                .id(100L)
                .flightNumber("BA123")
                .aircraft(aircraft)
                .crewMembers(Set.of(crewMember))
                .departureAirport("JFK")
                .arrivalAirport("LAX")
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now().plusHours(5))
                .build();

        requestDto = new FlightRequestDto(
                "BA123",
                1L,
                Set.of(10L),
                "JFK",
                "LAX",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5)
        );

        responseDto = new FlightResponseDto(
                100L,
                "BA123",
                "JFK",
                "LAX",
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                null,
                Set.of()
        );
    }

    @Test
    void testCreateFlight_success() {
        when(flightRepository.existsByFlightNumber("BA123")).thenReturn(false);
        when(aircraftRepository.findById(1L)).thenReturn(Optional.of(aircraft));
        when(crewMemberRepository.findById(10L)).thenReturn(Optional.of(crewMember));
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);
        when(flightMapper.toDto(flight)).thenReturn(responseDto);

        FlightResponseDto result = flightService.createFlight(requestDto);
        assertEquals(responseDto, result);
    }

    @Test
    void testCreateFlight_duplicate() {
        when(flightRepository.existsByFlightNumber("BA123")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> flightService.createFlight(requestDto));
    }

    @Test
    void testCreateFlight_aircraftNotFound() {
        when(flightRepository.existsByFlightNumber("BA123")).thenReturn(false);
        when(aircraftRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> flightService.createFlight(requestDto));
    }

    @Test
    void testCreateFlight_crewMemberNotFound() {
        when(flightRepository.existsByFlightNumber("BA123")).thenReturn(false);
        when(aircraftRepository.findById(1L)).thenReturn(Optional.of(aircraft));
        when(crewMemberRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> flightService.createFlight(requestDto));
    }

    @Test
    void testGetFlightById_success() {
        when(flightRepository.findById(100L)).thenReturn(Optional.of(flight));
        when(flightMapper.toDto(flight)).thenReturn(responseDto);

        FlightResponseDto result = flightService.getFlightById(100L);
        assertEquals(responseDto, result);
    }

    @Test
    void testGetFlightById_notFound() {
        when(flightRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> flightService.getFlightById(100L));
    }

    @Test
    void testGetAllFlights() {
        when(flightRepository.findAll()).thenReturn(List.of(flight));
        when(flightMapper.toDtoList(List.of(flight))).thenReturn(List.of(responseDto));

        List<FlightResponseDto> result = flightService.getAllFlights();
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateFlight_success() {
        when(flightRepository.findById(100L)).thenReturn(Optional.of(flight));
        when(aircraftRepository.findById(1L)).thenReturn(Optional.of(aircraft));
        when(crewMemberRepository.findById(10L)).thenReturn(Optional.of(crewMember));
        when(flightRepository.save(flight)).thenReturn(flight);
        when(flightMapper.toDto(flight)).thenReturn(responseDto);

        FlightResponseDto result = flightService.updateFlight(100L, requestDto);
        assertEquals(responseDto, result);
    }

    @Test
    void testUpdateFlight_notFound() {
        when(flightRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> flightService.updateFlight(100L, requestDto));
    }

    @Test
    void testDeleteFlight_success() {
        when(flightRepository.findById(100L)).thenReturn(Optional.of(flight));
        doNothing().when(flightRepository).delete(flight);

        assertDoesNotThrow(() -> flightService.deleteFlight(100L));
    }

    @Test
    void testDeleteFlight_notFound() {
        when(flightRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> flightService.deleteFlight(100L));
    }

    @Test
    void testMarkFlightAsDelayed_success() {
        flight.setFlightNumber("BA123");
        Booking booking = Booking.builder().flight(flight).passenger(passengerWithId(1L)).build();

        when(flightRepository.findById(100L)).thenReturn(Optional.of(flight));
        when(bookingRepository.findByFlight_Id(100L)).thenReturn(List.of(booking));
        when(flightRepository.save(flight)).thenReturn(flight);

        assertDoesNotThrow(() -> flightService.markFlightAsDelayed(100L, "Weather issue"));
        assertTrue(flight.isDelayed());
        assertEquals("Weather issue", flight.getDelayReason());
        verify(notificationService).send(any(NotificationRequest.class));
    }

    @Test
    void testMarkFlightAsDelayed_notFound() {
        when(flightRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> flightService.markFlightAsDelayed(100L, "Technical issue"));
    }

    private Passenger passengerWithId(Long id) {
        Passenger p = new Passenger();
        p.setId(id);
        p.setEmail("demo@baliair.com");
        return p;
    }
}

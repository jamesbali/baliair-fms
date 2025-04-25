package com.bali.baliairfms.service.impl;

import com.bali.baliairfms.dto.requestdto.FlightRequestDto;
import com.bali.baliairfms.dto.requestdto.NotificationRequest;
import com.bali.baliairfms.dto.responsedto.FlightResponseDto;
import com.bali.baliairfms.exception.DuplicateResourceException;
import com.bali.baliairfms.exception.ResourceNotFoundException;
import com.bali.baliairfms.mapper.FlightMapper;
import com.bali.baliairfms.model.Aircraft;
import com.bali.baliairfms.model.Booking;
import com.bali.baliairfms.model.CrewMember;
import com.bali.baliairfms.model.Flight;
import com.bali.baliairfms.model.enums.NotificationType;
import com.bali.baliairfms.repository.AircraftRepository;
import com.bali.baliairfms.repository.BookingRepository;
import com.bali.baliairfms.repository.CrewMemberRepository;
import com.bali.baliairfms.repository.FlightRepository;
import com.bali.baliairfms.service.FlightService;
import com.bali.baliairfms.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AircraftRepository aircraftRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final FlightMapper flightMapper;
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public FlightResponseDto createFlight(FlightRequestDto dto) {
        if (flightRepository.existsByFlightNumber(dto.flightNumber())) {
            throw new DuplicateResourceException("Flight number already exists: " + dto.flightNumber());
        }

        Aircraft aircraft = aircraftRepository.findById(dto.aircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + dto.aircraftId()));

        Set<CrewMember> crewMembers = dto.crewMemberIds().stream()
                .map(id -> crewMemberRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Crew member not found with id: " + id)))
                .collect(Collectors.toSet());

        Flight flight = Flight.builder()
                .flightNumber(dto.flightNumber())
                .aircraft(aircraft)
                .crewMembers(crewMembers)
                .departureAirport(dto.departureAirport())
                .arrivalAirport(dto.arrivalAirport())
                .departureTime(dto.departureTime())
                .arrivalTime(dto.arrivalTime())
                .build();

        return flightMapper.toDto(flightRepository.save(flight));
    }

    @Override
    public FlightResponseDto getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        return flightMapper.toDto(flight);
    }

    @Override
    public List<FlightResponseDto> getAllFlights() {
        return flightMapper.toDtoList(flightRepository.findAll());
    }

    @Override
    @Transactional
    public FlightResponseDto updateFlight(Long id, FlightRequestDto dto) {
        Flight existing = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

        Aircraft aircraft = aircraftRepository.findById(dto.aircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + dto.aircraftId()));

        Set<CrewMember> crewMembers = dto.crewMemberIds().stream()
                .map(cmId -> crewMemberRepository.findById(cmId)
                        .orElseThrow(() -> new ResourceNotFoundException("Crew member not found with id: " + cmId)))
                .collect(Collectors.toSet());

        existing.setFlightNumber(dto.flightNumber());
        existing.setAircraft(aircraft);
        existing.setCrewMembers(crewMembers);
        existing.setDepartureAirport(dto.departureAirport());
        existing.setArrivalAirport(dto.arrivalAirport());
        existing.setDepartureTime(dto.departureTime());
        existing.setArrivalTime(dto.arrivalTime());

        return flightMapper.toDto(flightRepository.save(existing));
    }


    @Override
    @Transactional
    public void deleteFlight(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        flightRepository.delete(flight);
    }

    @Override
    @Transactional
    public void markFlightAsDelayed(Long flightId, String reason) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        flight.setDelayed(true);
        flight.setDelayReason(reason);
        flightRepository.save(flight);

        List<Booking> bookings = bookingRepository.findByFlight_Id(flightId);
        for (Booking booking : bookings) {
            notificationService.send(new NotificationRequest(
                    booking.getPassenger().getId(),
                    "Flight Delayed",
                    "Your flight " + flight.getFlightNumber() + " has been delayed. Reason: " + reason,
                    NotificationType.EMAIL
            ));
        }
    }
}

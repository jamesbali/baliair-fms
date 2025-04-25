package com.bali.baliairfms.repository;

import com.bali.baliairfms.model.Aircraft;
import com.bali.baliairfms.model.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class FlightRepositoryTest {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    private Flight flight;

    @BeforeEach
    void setup() {
        Aircraft aircraft = new Aircraft();
        aircraft.setTailNumber("TAIL-F100");
        aircraft.setModel("Embraer E190");
        aircraft.setSeatingCapacity(100);
        aircraft.setRangeKm(4000);
        aircraft.setCurrentLocation("ORD");
        aircraft = aircraftRepository.save(aircraft);

        flight = new Flight();
        flight.setFlightNumber("FL123");
        flight.setAircraft(aircraft);
        flight.setCrewMembers(Set.of()); // assuming this is allowed
        flight.setDepartureAirport("ORD");
        flight.setArrivalAirport("ATL");
        flight.setDepartureTime(LocalDateTime.now());
        flight.setArrivalTime(LocalDateTime.now().plusHours(2));
        flight = flightRepository.save(flight);
    }

    @Test
    void testFindByFlightNumber_success() {
        Optional<Flight> result = flightRepository.findByFlightNumber("FL123");

        assertTrue(result.isPresent());
        assertEquals("ORD", result.get().getDepartureAirport());
    }

    @Test
    void testExistsByFlightNumber_true() {
        boolean exists = flightRepository.existsByFlightNumber("FL123");
        assertTrue(exists);
    }

    @Test
    void testExistsByFlightNumber_false() {
        boolean exists = flightRepository.existsByFlightNumber("NON_EXISTENT");
        assertFalse(exists);
    }
}

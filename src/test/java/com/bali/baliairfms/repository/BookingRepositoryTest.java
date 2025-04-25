package com.bali.baliairfms.repository;

import com.bali.baliairfms.model.Aircraft;
import com.bali.baliairfms.model.Booking;
import com.bali.baliairfms.model.Flight;
import com.bali.baliairfms.model.Passenger;
import com.bali.baliairfms.model.enums.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private PassengerRepository passengerRepository;
    @Autowired private FlightRepository flightRepository;
    @Autowired private AircraftRepository aircraftRepository;

    private Passenger passenger;
    private Flight flight;

    @BeforeEach
    void setup() {
        // ✅ Create and save a passenger
        passenger = new Passenger();
        passenger.setEmail("james.bali@baliair.com");
        passenger.setFirstName("James");
        passenger.setLastName("Bali");
        passenger.setPhoneNumber("1234567890");
        passenger.setPassword("securePass123");
        passenger.setActive(true);
        passenger = passengerRepository.save(passenger);

        // ✅ Create and save an aircraft
        Aircraft aircraft = new Aircraft();
        aircraft.setTailNumber("TAIL001");
        aircraft.setModel("Boeing 747");
        aircraft.setSeatingCapacity(300);
        aircraft.setRangeKm(14000);
        aircraft.setCurrentLocation("JFK");
        aircraft = aircraftRepository.save(aircraft);

        // ✅ Create and save a flight with the aircraft
        flight = new Flight();
        flight.setFlightNumber("BA123");
        flight.setAircraft(aircraft);
        flight.setCrewMembers(Set.of()); // assuming empty set is okay
        flight.setDepartureAirport("JFK");
        flight.setArrivalAirport("LAX");
        flight.setDepartureTime(LocalDateTime.now());
        flight.setArrivalTime(LocalDateTime.now().plusHours(5));
        flight = flightRepository.save(flight);
    }

    @Test
    void testFindByPassengerId() {
        Booking booking = saveBooking(passenger, flight);
        List<Booking> bookings = bookingRepository.findByPassenger_Id(passenger.getId());

        assertFalse(bookings.isEmpty());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void testFindByFlightId() {
        Booking booking = saveBooking(passenger, flight);
        List<Booking> bookings = bookingRepository.findByFlight_Id(flight.getId());

        assertFalse(bookings.isEmpty());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void testFindByPassengerIdAndFlightId() {
        Booking booking = saveBooking(passenger, flight);
        Optional<Booking> found = bookingRepository.findByPassenger_IdAndFlight_Id(passenger.getId(), flight.getId());

        assertTrue(found.isPresent());
        assertEquals(booking.getId(), found.get().getId());
    }

    @Test
    void testExistsByPassengerIdAndFlightId() {
        saveBooking(passenger, flight);
        boolean exists = bookingRepository.existsByPassenger_IdAndFlight_Id(passenger.getId(), flight.getId());
        assertTrue(exists);
    }

    private Booking saveBooking(Passenger passenger, Flight flight) {
        Booking booking = Booking.builder()
                .passenger(passenger)
                .flight(flight)
                .seatNumber("12A")
                .bookingTime(LocalDateTime.now())
                .status(BookingStatus.BOOKED)
                .build();
        return bookingRepository.save(booking);
    }
}

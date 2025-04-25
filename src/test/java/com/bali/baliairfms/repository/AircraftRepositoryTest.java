package com.bali.baliairfms.repository;

import com.bali.baliairfms.model.Aircraft;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@DataJpaTest
class AircraftRepositoryTest {

    @Autowired
    private AircraftRepository aircraftRepository;

    @Test
    void testFindByTailNumber_success() {
        // Arrange
        Aircraft aircraft = new Aircraft();
        aircraft.setTailNumber("TAIL001");
        aircraft.setModel("Boeing 737");
        aircraft.setSeatingCapacity(180);
        aircraft.setRangeKm(6000);
        aircraft.setCurrentLocation("JFK");

        aircraftRepository.save(aircraft);

        // Act
        Optional<Aircraft> result = aircraftRepository.findByTailNumber("TAIL001");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Boeing 737", result.get().getModel());
    }

    @Test
    void testExistsByTailNumber_true() {
        Aircraft aircraft = new Aircraft();
        aircraft.setTailNumber("TAIL002");
        aircraft.setModel("Airbus A320");
        aircraft.setSeatingCapacity(150);
        aircraft.setRangeKm(5500);
        aircraft.setCurrentLocation("LAX");

        aircraftRepository.save(aircraft);

        boolean exists = aircraftRepository.existsByTailNumber("TAIL002");
        assertTrue(exists);
    }

    @Test
    void testExistsByTailNumber_false() {
        boolean exists = aircraftRepository.existsByTailNumber("NON_EXISTENT");
        assertFalse(exists);
    }
}

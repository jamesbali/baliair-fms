package com.bali.baliairfms.repository;

import com.bali.baliairfms.model.Passenger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PassengerRepositoryTest {

    @Autowired
    private PassengerRepository passengerRepository;

    @Test
    void testFindByEmail_success() {
        Passenger passenger = new Passenger();
        passenger.setEmail("jane.doe@baliair.com");
        passenger.setFirstName("Jane");
        passenger.setLastName("Doe");
        passenger.setPhoneNumber("9876543210");
        passenger.setPassword("securePassword");
        passenger.setActive(true);

        passengerRepository.save(passenger);

        Optional<Passenger> result = passengerRepository.findByEmail("jane.doe@baliair.com");

        assertTrue(result.isPresent());
        assertEquals("Jane", result.get().getFirstName());
    }

    @Test
    void testExistsByEmail_true() {
        Passenger passenger = new Passenger();
        passenger.setEmail("bob.brown@baliair.com");
        passenger.setFirstName("Bob");
        passenger.setLastName("Brown");
        passenger.setPhoneNumber("1231231234");
        passenger.setPassword("strongPassword");
        passenger.setActive(true);

        passengerRepository.save(passenger);

        boolean exists = passengerRepository.existsByEmail("bob.brown@baliair.com");
        assertTrue(exists);
    }

    @Test
    void testExistsByEmail_false() {
        boolean exists = passengerRepository.existsByEmail("nonexistent@baliair.com");
        assertFalse(exists);
    }
}

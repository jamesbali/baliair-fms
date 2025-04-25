package com.bali.baliairfms.repository;

import com.bali.baliairfms.model.CrewMember;
import com.bali.baliairfms.model.enums.CrewType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CrewMemberRepositoryTest {

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Test
    void testFindByStaffId_success() {
        CrewMember crew = new CrewMember();
        crew.setStaffId("CM1001");
        crew.setFirstName("Alice");
        crew.setLastName("Smith");
        crew.setContactInfo("alice.smith@baliair.com");
        crew.setCertifications(Set.of("CPR", "Flight Safety"));
        crew.setQualifications(Set.of("Senior Flight Attendant"));
        crew.setAvailable(true);
        crew.setTotalFlightHours(1500);
        crew.setCrewType(CrewType.FLIGHT_ATTENDANT);

        crewMemberRepository.save(crew);

        Optional<CrewMember> result = crewMemberRepository.findByStaffId("CM1001");

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getFirstName());
    }

    @Test
    void testExistsByStaffId_true() {
        CrewMember crew = new CrewMember();
        crew.setStaffId("CM1002");
        crew.setFirstName("Bob");
        crew.setLastName("Jones");
        crew.setContactInfo("bob.jones@baliair.com");
        crew.setCertifications(Set.of("CPR"));
        crew.setQualifications(Set.of("Pilot"));
        crew.setAvailable(true);
        crew.setTotalFlightHours(2000);
        crew.setCrewType(CrewType.PILOT);

        crewMemberRepository.save(crew);

        boolean exists = crewMemberRepository.existsByStaffId("CM1002");
        assertTrue(exists);
    }

    @Test
    void testExistsByStaffId_false() {
        boolean exists = crewMemberRepository.existsByStaffId("UNKNOWN_STAFF_ID");
        assertFalse(exists);
    }
}

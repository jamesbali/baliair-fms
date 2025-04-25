package com.bali.baliairfms.repository;

import com.bali.baliairfms.model.Aircraft;
import com.bali.baliairfms.model.MaintenanceRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class MaintenanceRecordRepositoryTest {

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    private Aircraft aircraft;

    @BeforeEach
    void setUp() {
        aircraft = new Aircraft();
        aircraft.setTailNumber("MX-T001");
        aircraft.setModel("Boeing 777");
        aircraft.setSeatingCapacity(396);
        aircraft.setRangeKm(15600);
        aircraft.setCurrentLocation("SEA");

        aircraft = aircraftRepository.save(aircraft);
    }

    @Test
    void testFindByAircraftId_returnsRecords() {
        MaintenanceRecord record1 = new MaintenanceRecord();
        record1.setAircraft(aircraft);
        record1.setMaintenanceDate(LocalDate.of(2024, 2, 1));
        record1.setDescription("Landing gear check");
        record1.setCompleted(true);

        MaintenanceRecord record2 = new MaintenanceRecord();
        record2.setAircraft(aircraft);
        record2.setMaintenanceDate(LocalDate.of(2024, 3, 10));
        record2.setDescription("Cabin pressure system maintenance");
        record2.setCompleted(false);

        maintenanceRecordRepository.saveAll(List.of(record1, record2));

        List<MaintenanceRecord> records = maintenanceRecordRepository.findByAircraft_Id(aircraft.getId());

        assertEquals(2, records.size());
        assertTrue(records.stream().anyMatch(r -> r.getDescription().contains("Landing gear")));
    }

    @Test
    void testFindByAircraftId_noRecords() {
        List<MaintenanceRecord> records = maintenanceRecordRepository.findByAircraft_Id(999L);
        assertTrue(records.isEmpty());
    }
}

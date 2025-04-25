package com.bali.baliairfms.repository;

import com.bali.baliairfms.model.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {

    List<MaintenanceRecord> findByAircraft_Id(Long aircraftId);

}

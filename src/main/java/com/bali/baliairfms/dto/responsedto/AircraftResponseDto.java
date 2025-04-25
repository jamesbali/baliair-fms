package com.bali.baliairfms.dto.responsedto;

import java.util.List;

public record AircraftResponseDto (

        Long id,
        String tailNumber,
        String model,
        int seatingCapacity,
        int rangeKm,
        String currentLocation,
        List<MaintenanceRecordDto> maintenanceRecords
){
}

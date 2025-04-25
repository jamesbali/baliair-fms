package com.bali.baliairfms.dto.responsedto;

import java.time.LocalDate;

public record MaintenanceRecordDto(
        Long id,
        LocalDate maintenanceDate,
        String description,
        boolean completed
) {
}

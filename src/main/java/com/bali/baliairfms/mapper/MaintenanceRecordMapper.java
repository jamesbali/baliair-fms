package com.bali.baliairfms.mapper;


import com.bali.baliairfms.dto.responsedto.MaintenanceRecordDto;
import com.bali.baliairfms.model.MaintenanceRecord;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MaintenanceRecordMapper {

    MaintenanceRecordDto toDto(MaintenanceRecord record);

    List<MaintenanceRecordDto> toDtoList(List<MaintenanceRecord> records);
}

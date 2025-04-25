package com.bali.baliairfms.mapper;

import com.bali.baliairfms.dto.requestdto.AircraftRequestDto;
import com.bali.baliairfms.dto.responsedto.AircraftResponseDto;
import com.bali.baliairfms.model.Aircraft;
import org.mapstruct.BeanMapping;
import org.mapstruct.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = { MaintenanceRecordMapper.class })
public interface AircraftMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "maintenanceRecords", ignore = true)
    Aircraft toEntity(AircraftRequestDto dto);

    AircraftResponseDto toDto(Aircraft aircraft);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "maintenanceRecords", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAircraftFromDto(AircraftRequestDto dto, @MappingTarget Aircraft aircraft);
}

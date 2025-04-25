package com.bali.baliairfms.mapper;

import com.bali.baliairfms.dto.responsedto.FlightResponseDto;
import com.bali.baliairfms.model.Flight;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { AircraftMapper.class, CrewMemberMapper.class })
public interface FlightMapper {

    FlightResponseDto toDto(Flight flight);

    List<FlightResponseDto> toDtoList(List<Flight> flights);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Flight flight, Flight updatedFlight);
}

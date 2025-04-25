package com.bali.baliairfms.mapper;


import com.bali.baliairfms.dto.requestdto.PassengerRequestDto;
import com.bali.baliairfms.dto.responsedto.PassengerResponseDto;
import com.bali.baliairfms.model.Passenger;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    Passenger toEntity(PassengerRequestDto dto);

    PassengerResponseDto toDto(Passenger passenger);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePassengerFromDto(PassengerRequestDto dto, @MappingTarget Passenger passenger);
}

package com.bali.baliairfms.mapper;

import com.bali.baliairfms.dto.requestdto.CrewMemberRequestDto;
import com.bali.baliairfms.dto.responsedto.CrewMemberResponseDto;
import com.bali.baliairfms.model.CrewMember;
import org.mapstruct.BeanMapping;
import org.mapstruct.*;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CrewMemberMapper {

    @Mapping(target = "id", ignore = true)
    CrewMember toEntity(CrewMemberRequestDto dto);

    CrewMemberResponseDto toDto(CrewMember entity);

    //  Convert list of CrewMember to list of DTOs
    List<CrewMemberResponseDto> toDtoList(List<CrewMember> crewList);

    // Updating: merge fields from DTO into existing entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CrewMemberRequestDto dto, @MappingTarget CrewMember entity);
}

package com.bali.baliairfms.dto.responsedto;

import com.bali.baliairfms.model.enums.CrewType;

import java.util.Set;

public record CrewMemberResponseDto(
        Long id,
        String staffId,
        String firstName,
        String lastName,
        String contactInfo,
        Set<String> certifications,
        Set<String> qualifications,
        boolean available,
        int totalFlightHours,
        CrewType crewType
) {
}

package com.bali.baliairfms.dto.requestdto;

import com.bali.baliairfms.model.enums.CrewType;
import jakarta.validation.constraints.*;
import java.util.*;

public record CrewMemberRequestDto(
        @NotBlank(message = "Staff ID is required")
        String staffId,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Contact info is required")
        String contactInfo,

        @NotNull(message = "Certifications are required")
        Set<@NotBlank(message = "Certification value cannot be blank") String> certifications,

        @NotNull(message = "Qualifications are required")
        Set<@NotBlank(message = "Qualification value cannot be blank") String> qualifications,

        @NotNull(message = "Availability must be specified")
        Boolean available,

        @Min(value = 0, message = "Total flight hours must be zero or positive")
        int totalFlightHours,

        @NotNull(message = "Crew type is required")
        CrewType crewType
) {
}

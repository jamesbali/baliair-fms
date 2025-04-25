package com.bali.baliairfms.specification;

import com.bali.baliairfms.dto.requestdto.FlightSearchDto;
import com.bali.baliairfms.model.Flight;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class FlightSpecification {

    public static Specification<Flight> withFilters(FlightSearchDto dto) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (dto.flightNumber() != null && !dto.flightNumber().isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("flightNumber")), "%" + dto.flightNumber().toLowerCase() + "%"));
            }

            if (dto.departureAirport() != null && !dto.departureAirport().isEmpty()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("departureAirport")),
                                "%" + dto.departureAirport().toLowerCase() + "%"));
            }

            if (dto.arrivalAirport() != null && !dto.arrivalAirport().isEmpty()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("arrivalAirport")),
                                "%" + dto.arrivalAirport().toLowerCase() + "%"));
            }

            if (dto.departureDate() != null) {
                // Create date-time range: 2025-04-30 00:00 to 2025-04-30 23:59
                LocalDateTime startOfDay = dto.departureDate().atStartOfDay();
                LocalDateTime endOfDay = dto.departureDate().atTime(23, 59, 59);

                predicate = cb.and(predicate,
                        cb.between(root.get("departureTime"), startOfDay, endOfDay));
            }

            return predicate;
        };
    }
}

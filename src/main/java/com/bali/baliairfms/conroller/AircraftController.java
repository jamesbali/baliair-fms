package com.bali.baliairfms.conroller;

import com.bali.baliairfms.dto.requestdto.AircraftRequestDto;
import com.bali.baliairfms.dto.responsedto.AircraftResponseDto;
import com.bali.baliairfms.service.AircraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/aircraft")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AircraftController {

    private final AircraftService aircraftService;

    // Create aircraft
    @PostMapping
    public ResponseEntity<AircraftResponseDto> createAircraft(@Valid @RequestBody AircraftRequestDto dto) {
        AircraftResponseDto created = aircraftService.createAircraft(dto);
        return ResponseEntity.created(URI.create("/api/v1/aircraft/" + created.id())).body(created);
    }

    // Get aircraft by ID
    @GetMapping("/{id}")
    public ResponseEntity<AircraftResponseDto> getAircraftById(@PathVariable Long id) {
        return ResponseEntity.ok(aircraftService.getAircraftById(id));
    }

    // Get all aircraft
    @GetMapping
    public ResponseEntity<List<AircraftResponseDto>> getAllAircraft() {
        return ResponseEntity.ok(aircraftService.getAllAircraft());
    }

    // Update aircraft
    @PutMapping("/{id}")
    public ResponseEntity<AircraftResponseDto> updateAircraft(@PathVariable Long id,
                                                              @Valid @RequestBody AircraftRequestDto dto) {
        return ResponseEntity.ok(aircraftService.updateAircraft(id, dto));
    }

    // Delete aircraft
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
        aircraftService.deleteAircraft(id);
        return ResponseEntity.noContent().build();
    }

}

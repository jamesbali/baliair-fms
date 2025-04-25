package com.bali.baliairfms.conroller;

import com.bali.baliairfms.dto.requestdto.PassengerRequestDto;
import com.bali.baliairfms.dto.responsedto.PassengerResponseDto;
import com.bali.baliairfms.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    //  Register a new passenger
    @PostMapping
    public ResponseEntity<PassengerResponseDto> registerPassenger(@Valid @RequestBody PassengerRequestDto dto) {
        PassengerResponseDto created = passengerService.registerPassenger(dto);
        return ResponseEntity.created(URI.create("/api/v1/passengers/" + created.id())).body(created);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/register")
    public ResponseEntity<PassengerResponseDto> selfRegisterPassenger(@Valid @RequestBody PassengerRequestDto dto) {
        PassengerResponseDto created = passengerService.registerPassenger(dto);
        return ResponseEntity.created(URI.create("/api/v1/passengers/" + created.id())).body(created);
    }


    //  Get passenger by ID
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponseDto> getPassengerById(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }

    // List all passengers
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PassengerResponseDto>> getAllPassengers() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    // Update passenger
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponseDto> updatePassenger(@PathVariable Long id,
                                                                @Valid @RequestBody PassengerRequestDto dto) {
        return ResponseEntity.ok(passengerService.updatePassenger(id, dto));
    }

    //  Delete passenger
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
}

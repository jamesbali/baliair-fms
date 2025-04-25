package com.bali.baliairfms.conroller;

import com.bali.baliairfms.dto.requestdto.CrewMemberRequestDto;
import com.bali.baliairfms.dto.responsedto.CrewMemberResponseDto;
import com.bali.baliairfms.service.CrewMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/crew")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CrewMemberController {

    private final CrewMemberService crewMemberService;

    //  Create crew member
    @PostMapping
    public ResponseEntity<CrewMemberResponseDto> create(@Valid @RequestBody CrewMemberRequestDto dto) {
        CrewMemberResponseDto created = crewMemberService.createCrewMember(dto);
        return ResponseEntity.created(URI.create("/api/v1/crew/" + created.id())).body(created);
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<CrewMemberResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(crewMemberService.getCrewMemberById(id));
    }

    //  Get all
    @GetMapping
    public ResponseEntity<List<CrewMemberResponseDto>> getAll() {
        return ResponseEntity.ok(crewMemberService.getAllCrewMembers());
    }

    //  Update
    @PutMapping("/{id}")
    public ResponseEntity<CrewMemberResponseDto> update(@PathVariable Long id,
                                                        @Valid @RequestBody CrewMemberRequestDto dto) {
        return ResponseEntity.ok(crewMemberService.updateCrewMember(id, dto));
    }

    //  Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        crewMemberService.deleteCrewMember(id);
        return ResponseEntity.noContent().build();
    }
}

package com.bali.baliairfms.service.impl;

import com.bali.baliairfms.dto.requestdto.CrewMemberRequestDto;
import com.bali.baliairfms.dto.responsedto.CrewMemberResponseDto;
import com.bali.baliairfms.exception.DuplicateResourceException;
import com.bali.baliairfms.exception.ResourceNotFoundException;
import com.bali.baliairfms.mapper.CrewMemberMapper;
import com.bali.baliairfms.model.CrewMember;
import com.bali.baliairfms.model.enums.CrewType;
import com.bali.baliairfms.repository.CrewMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

class CrewMemberServiceImplTest {

    @Mock
    private CrewMemberRepository repository;

    @Mock
    private CrewMemberMapper mapper;

    @InjectMocks
    private CrewMemberServiceImpl service;

    private CrewMember crew;
    private CrewMemberRequestDto requestDto;
    private CrewMemberResponseDto responseDto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        crew = new CrewMember();
        crew.setId(1L);

        requestDto = new CrewMemberRequestDto(
                "PILOT123",                        // staffId
                "John",                            // firstName
                "Doe",                             // lastName
                "john.doe@baliair.com",            // contactInfo
                Set.of("Certified Pilot"),         // certifications
                Set.of("Jet Qualified"),           // qualifications
                true,                              // available
                1200,                              // totalFlightHours
                CrewType.PILOT                     // crewType
        );

        responseDto = new CrewMemberResponseDto(
                1L,
                "John",
                "Doe",
                "PILOT123",
                "john.doe@baliair.com",
                Set.of("Certified Pilot"),
                Set.of("Jet Qualified"),
                true,
                1200,
                CrewType.PILOT
        );
    }

    @Test
    void testCreateCrewMember_success() {
        when(repository.existsByStaffId(requestDto.staffId())).thenReturn(false);
        when(mapper.toEntity(requestDto)).thenReturn(crew);
        when(repository.save(crew)).thenReturn(crew);
        when(mapper.toDto(crew)).thenReturn(responseDto);

        CrewMemberResponseDto result = service.createCrewMember(requestDto);

        assertEquals(responseDto, result);
        verify(repository).save(crew);
    }

    @Test
    void testCreateCrewMember_duplicateStaffId() {
        when(repository.existsByStaffId(requestDto.staffId())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.createCrewMember(requestDto));
    }

    @Test
    void testGetCrewMemberById_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(crew));
        when(mapper.toDto(crew)).thenReturn(responseDto);

        CrewMemberResponseDto result = service.getCrewMemberById(1L);

        assertEquals(responseDto, result);
    }

    @Test
    void testGetCrewMemberById_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getCrewMemberById(1L));
    }

    @Test
    void testGetAllCrewMembers() {
        when(repository.findAll()).thenReturn(List.of(crew));
        when(mapper.toDtoList(List.of(crew))).thenReturn(List.of(responseDto));

        List<CrewMemberResponseDto> result = service.getAllCrewMembers();

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
    }

    @Test
    void testUpdateCrewMember_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(crew));
        doNothing().when(mapper).updateEntityFromDto(requestDto, crew);
        when(repository.save(crew)).thenReturn(crew);
        when(mapper.toDto(crew)).thenReturn(responseDto);

        CrewMemberResponseDto result = service.updateCrewMember(1L, requestDto);

        assertEquals(responseDto, result);
    }

    @Test
    void testUpdateCrewMember_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateCrewMember(1L, requestDto));
    }

    @Test
    void testDeleteCrewMember_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(crew));
        doNothing().when(repository).delete(crew);

        assertDoesNotThrow(() -> service.deleteCrewMember(1L));
        verify(repository).delete(crew);
    }

    @Test
    void testDeleteCrewMember_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteCrewMember(1L));
    }
}

package com.bali.baliairfms.service.impl;

import com.bali.baliairfms.dto.requestdto.CrewMemberRequestDto;
import com.bali.baliairfms.dto.responsedto.CrewMemberResponseDto;
import com.bali.baliairfms.exception.DuplicateResourceException;
import com.bali.baliairfms.exception.ResourceNotFoundException;
import com.bali.baliairfms.mapper.CrewMemberMapper;
import com.bali.baliairfms.model.CrewMember;
import com.bali.baliairfms.repository.CrewMemberRepository;
import com.bali.baliairfms.service.CrewMemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrewMemberServiceImpl implements CrewMemberService {

    private final CrewMemberRepository repository;
    private final CrewMemberMapper mapper;

    @Override
    @Transactional
    public CrewMemberResponseDto createCrewMember(CrewMemberRequestDto dto) {
        if (repository.existsByStaffId(dto.staffId())) {
            throw new DuplicateResourceException("Staff ID already exists: " + dto.staffId());
        }
        CrewMember crew = mapper.toEntity(dto);
        return mapper.toDto(repository.save(crew));
    }

    @Override
    public CrewMemberResponseDto getCrewMemberById(Long id) {
        CrewMember crew = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crew member not found with id: " + id));
        return mapper.toDto(crew);
    }

    @Override
    public List<CrewMemberResponseDto> getAllCrewMembers() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    @Transactional
    public CrewMemberResponseDto updateCrewMember(Long id, CrewMemberRequestDto dto) {
        CrewMember crew = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crew member not found with id: " + id));
        mapper.updateEntityFromDto(dto, crew);
        return mapper.toDto(repository.save(crew));
    }

    @Override
    @Transactional
    public void deleteCrewMember(Long id) {
        CrewMember crew = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crew member not found with id: " + id));
        repository.delete(crew);
    }
}

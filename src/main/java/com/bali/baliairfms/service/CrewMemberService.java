package com.bali.baliairfms.service;

import com.bali.baliairfms.dto.requestdto.CrewMemberRequestDto;
import com.bali.baliairfms.dto.responsedto.CrewMemberResponseDto;

import java.util.List;

public interface CrewMemberService {

    CrewMemberResponseDto createCrewMember(CrewMemberRequestDto dto);

    CrewMemberResponseDto getCrewMemberById(Long id);

    List<CrewMemberResponseDto> getAllCrewMembers();

    CrewMemberResponseDto updateCrewMember(Long id, CrewMemberRequestDto dto);

    void deleteCrewMember(Long id);
}

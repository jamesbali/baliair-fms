package com.bali.baliairfms.conroller;

import com.bali.baliairfms.dto.requestdto.CrewMemberRequestDto;
import com.bali.baliairfms.dto.responsedto.CrewMemberResponseDto;
import com.bali.baliairfms.model.enums.CrewType;
import com.bali.baliairfms.security.jwt.JwtService;
import com.bali.baliairfms.service.CrewMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CrewMemberController.class)
@AutoConfigureMockMvc(addFilters = false)
class CrewMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private CrewMemberService crewMemberService;
    @MockBean private JwtService jwtService;

    private final CrewMemberRequestDto requestDto = new CrewMemberRequestDto(
            "STAFF123",
            "John",
            "Doe",
            "john.doe@baliair.com",
            Set.of("CPR", "Evac Training"),
            Set.of("International License"),
            true,
            1200,
            CrewType.PILOT
    );

    private final CrewMemberResponseDto responseDto = new CrewMemberResponseDto(
            1L,
            "STAFF123",
            "John",
            "Doe",
            "john.doe@baliair.com",
            Set.of("CPR", "Evac Training"),
            Set.of("International License"),
            true,
            1200,
            CrewType.PILOT
    );

    @WithMockUser(roles = "ADMIN")
    @Test
    void testCreateCrewMember() throws Exception {
        when(crewMemberService.createCrewMember(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/crew")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testGetById() throws Exception {
        when(crewMemberService.getCrewMemberById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/crew/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testGetAllCrewMembers() throws Exception {
        when(crewMemberService.getAllCrewMembers()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/crew"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].staffId").value("STAFF123"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testUpdateCrewMember() throws Exception {
        when(crewMemberService.updateCrewMember(eq(1L), any())).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/crew/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.crewType").value("PILOT"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testDeleteCrewMember() throws Exception {
        doNothing().when(crewMemberService).deleteCrewMember(1L);

        mockMvc.perform(delete("/api/v1/crew/1"))
                .andExpect(status().isNoContent());
    }
}

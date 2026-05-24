/*package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Member.*;
import com.SUNData.MemberApp.Service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    private MemberDetailsDTO memberDetailsDTO;
    private PrincipalMemberDTO principalMemberDTO;
    private NextOfKinDTO nextOfKinDTO;
    private DependantDTO dependantDTO;

    @BeforeEach
    void setUp() {
        memberDetailsDTO = new MemberDetailsDTO();
        principalMemberDTO = new PrincipalMemberDTO();
        nextOfKinDTO = new NextOfKinDTO();
        dependantDTO = new DependantDTO();
    }

    // ==========================================
    // VALIDATION & REGISTRATION TESTS
    // ==========================================

    @Test
    @WithMockUser(authorities = "MEMBER_CREATE")
    @DisplayName("POST /register - Success with Valid Payload")
    void registerMember_WithValidPayload_ShouldReturnOk() throws Exception {
        RegisterMemberRequestDTO validRequest = new RegisterMemberRequestDTO();
        // TODO: Populate mandatory mock fields here to pass your specific DTO validations
        // validRequest.setNationalId("12345678");

        Mockito.when(memberService.registerMember(any(RegisterMemberRequestDTO.class)))
                .thenReturn(memberDetailsDTO);

        mockMvc.perform(post("/api/v1/members/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "MEMBER_CREATE")
    @DisplayName("POST /register - Failure with Invalid Payload (400 Bad Request)")
    void registerMember_WithInvalidPayload_ShouldReturnBadRequest() throws Exception {
        // Intentionally leaving fields blank to trigger validation failures
        RegisterMemberRequestDTO invalidRequest = new RegisterMemberRequestDTO();

        mockMvc.perform(post("/api/v1/members/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    // ==========================================
    // READ OPERATIONS (GET)
    // ==========================================

    @Test
    @WithMockUser(authorities = "MEMBER_READ")
    @DisplayName("GET / - Fetch All Members")
    void getAllMembers_ShouldReturnList() throws Exception {
        List<MemberDetailsDTO> members = Collections.singletonList(memberDetailsDTO);
        Mockito.when(memberService.getAllMembers()).thenReturn(members);

        mockMvc.perform(get("/api/v1/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(authorities = "MEMBER_READ")
    @DisplayName("GET /{id} - Fetch Member by ID")
    void getMemberById_ShouldReturnMember() throws Exception {
        Mockito.when(memberService.getMemberById(1L)).thenReturn(memberDetailsDTO);

        mockMvc.perform(get("/api/v1/members/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "MEMBER_READ")
    @DisplayName("GET /search/{nationalId} - Search Member by National ID")
    void getMemberByNationalId_ShouldReturnMember() throws Exception {
        Mockito.when(memberService.getMemberByNationalId("12345678")).thenReturn(memberDetailsDTO);

        mockMvc.perform(get("/api/v1/members/search/{nationalId}", "12345678"))
                .andExpect(status().isOk());
    }

    // ==========================================
    // PRINCIPAL OPERATIONS (PUT / PATCH)
    // ==========================================

    @Test
    @WithMockUser(authorities = "MEMBER_WRITE")
    @DisplayName("PUT /{id} - Update Principal Profile")
    void updatePrincipal_ShouldReturnUpdatedDTO() throws Exception {
        Mockito.when(memberService.updatePrincipal(eq(1L), any(PrincipalMemberDTO.class)))
                .thenReturn(principalMemberDTO);

        mockMvc.perform(put("/api/v1/members/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(principalMemberDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "MEMBER_WRITE")
    @DisplayName("PATCH /{id} - Partially Update Principal Profile")
    void patchPrincipal_ShouldReturnPatchedDTO() throws Exception {
        Mockito.when(memberService.patchPrincipal(eq(1L), any(PrincipalMemberDTO.class)))
                .thenReturn(principalMemberDTO);

        mockMvc.perform(patch("/api/v1/members/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(principalMemberDTO)))
                .andExpect(status().isOk());
    }

    // ==========================================
    // NEXT OF KIN OPERATIONS
    // ==========================================

    @Test
    @WithMockUser(authorities = "MEMBER_WRITE")
    @DisplayName("PUT /{id}/next-of-kin - Update Next Of Kin")
    void updateNextOfKin_ShouldReturnUpdatedDTO() throws Exception {
        Mockito.when(memberService.updateNextOfKin(eq(1L), any(NextOfKinDTO.class)))
                .thenReturn(nextOfKinDTO);

        mockMvc.perform(put("/api/v1/members/{principalId}/next-of-kin", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nextOfKinDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "MEMBER_WRITE")
    @DisplayName("DELETE /{id}/next-of-kin - Remove Next Of Kin")
    void deleteNextOfKin_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(memberService).deleteNextOfKin(1L);

        mockMvc.perform(delete("/api/v1/members/{principalId}/next-of-kin", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    // ==========================================
    // DEPENDANT OPERATIONS
    // ==========================================

    @Test
    @WithMockUser(authorities = "MEMBER_WRITE")
    @DisplayName("POST /{id}/dependants - Add Dependant")
    void addDependant_ShouldReturnCreatedDTO() throws Exception {
        Mockito.when(memberService.addDependant(eq(1L), any(DependantDTO.class)))
                .thenReturn(dependantDTO);

        mockMvc.perform(post("/api/v1/members/{principalId}/dependants", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dependantDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "MEMBER_WRITE")
    @DisplayName("DELETE /{pId}/dependants/{dId} - Remove Dependant")
    void deleteDependant_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(memberService).deleteDependant(1L, 2L);

        mockMvc.perform(delete("/api/v1/members/{principalId}/dependants/{dependantId}", 1L, 2L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    // ==========================================
    // SECURITY AUTHENTICATION & AUTHORIZATION TESTS
    // ==========================================

    @Test
    @DisplayName("ANY - Request without authentication should return 401 Unauthorized")
    void endpoint_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/members"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "INVALID_AUTHORITY")
    @DisplayName("ANY - Request with wrong authority should return 403 Forbidden")
    void endpoint_WithWrongAuthority_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/members"))
                .andExpect(status().isForbidden());
    }
}
*/
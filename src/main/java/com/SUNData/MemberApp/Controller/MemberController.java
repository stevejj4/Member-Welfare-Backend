package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Member.*;
import com.SUNData.MemberApp.Service.member.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('MEMBER_CREATE')")
    public ResponseEntity<MemberDetailsDTO> registerMember(
            @Valid @RequestBody RegisterMemberRequestDTO request) {
        return ResponseEntity.ok(memberService.registerMember(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MEMBER_READ')")
    public ResponseEntity<List<MemberDetailsDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/exists")
    @PreAuthorize("hasAnyAuthority('MEMBER_READ', 'MEMBER_CREATE')")
    public ResponseEntity<MemberExistsResponseDTO> memberExists(
            @RequestParam(required = false) String nationalId,
            @RequestParam(required = false) String phoneNumber) {
        return ResponseEntity.ok(memberService.memberExists(nationalId, phoneNumber));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MEMBER_READ')")
    public ResponseEntity<MemberDetailsDTO> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @GetMapping("/search/{nationalId}")
    @PreAuthorize("hasAuthority('MEMBER_READ')")
    public ResponseEntity<MemberDetailsDTO> getMemberByNationalId(@PathVariable String nationalId) {
        return ResponseEntity.ok(memberService.getMemberByNationalId(nationalId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MEMBER_WRITE')")
    public ResponseEntity<PrincipalMemberDTO> updatePrincipal(
            @PathVariable Long id,
            @Valid @RequestBody PrincipalMemberDTO dto) {
        return ResponseEntity.ok(memberService.updatePrincipal(id, dto));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('MEMBER_WRITE')")
    public ResponseEntity<PrincipalMemberDTO> patchPrincipal(
            @PathVariable Long id,
            @Valid @RequestBody PrincipalMemberDTO dto) {
        return ResponseEntity.ok(memberService.patchPrincipal(id, dto));
    }

    @PutMapping("/{principalId}/next-of-kin")
    @PreAuthorize("hasAuthority('MEMBER_WRITE')")
    public ResponseEntity<NextOfKinDTO> updateNextOfKin(
            @PathVariable Long principalId,
            @Valid @RequestBody NextOfKinDTO dto) {
        return ResponseEntity.ok(memberService.updateNextOfKin(principalId, dto));
    }

    @PatchMapping("/{principalId}/next-of-kin")
    @PreAuthorize("hasAuthority('MEMBER_WRITE')")
    public ResponseEntity<NextOfKinDTO> patchNextOfKin(
            @PathVariable Long principalId,
            @Valid @RequestBody NextOfKinDTO dto) {
        return ResponseEntity.ok(memberService.patchNextOfKin(principalId, dto));
    }

    @DeleteMapping("/{principalId}/next-of-kin")
    @PreAuthorize("hasAuthority('MEMBER_WRITE')")
    public ResponseEntity<Void> deleteNextOfKin(@PathVariable Long principalId) {
        memberService.deleteNextOfKin(principalId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{principalId}/dependants")
    @PreAuthorize("hasAuthority('MEMBER_WRITE')")
    public ResponseEntity<DependantDTO> addDependant(
            @PathVariable Long principalId,
            @Valid @RequestBody DependantDTO dto) {
        return ResponseEntity.ok(memberService.addDependant(principalId, dto));
    }

    @PatchMapping("/dependants/{dependantId}")
    @PreAuthorize("hasAuthority('MEMBER_WRITE')")
    public ResponseEntity<DependantDTO> patchDependant(
            @PathVariable Long dependantId,
            @Valid @RequestBody DependantDTO dto) {
        return ResponseEntity.ok(memberService.patchDependant(dependantId, dto));
    }

    @DeleteMapping("/{principalId}/dependants/{dependantId}")
    @PreAuthorize("hasAuthority('MEMBER_WRITE')")
    public ResponseEntity<Void> deleteDependant(
            @PathVariable Long principalId,
            @PathVariable Long dependantId) {
        memberService.deleteDependant(principalId, dependantId);
        return ResponseEntity.noContent().build();
    }
}

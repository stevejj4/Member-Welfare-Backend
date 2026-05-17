package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Member.*;
import com.SUNData.MemberApp.Service.auth.FacilitatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilitator")
public class FacilitatorController {

    private final FacilitatorService facilitatorService;

    public FacilitatorController(FacilitatorService facilitatorService) {
        this.facilitatorService = facilitatorService;
    }

    // ✅ Register new member
    @PostMapping("/register")
    public ResponseEntity<MemberDetailsDTO> registerMember(@Valid @RequestBody RegisterMemberRequestDTO request) {
        return ResponseEntity.ok(facilitatorService.registerMember(request));
    }

    // ✅ Update principal member (full update)
    @PutMapping("/members/{id}")
    public ResponseEntity<PrincipalMemberDTO> updatePrincipal(@PathVariable Long id,
                                                              @RequestBody PrincipalMemberDTO dto) {
        return ResponseEntity.ok(facilitatorService.updatePrincipal(id, dto));
    }

    // ✅ Patch principal member (partial update)
    @PatchMapping("/members/{id}")
    public ResponseEntity<PrincipalMemberDTO> patchPrincipal(@PathVariable Long id,
                                                             @RequestBody PrincipalMemberDTO dto) {
        return ResponseEntity.ok(facilitatorService.patchPrincipal(id, dto));
    }

    // ✅ View all members
    @GetMapping("/members")
    public ResponseEntity<List<MemberDetailsDTO>> getAllMembers() {
        return ResponseEntity.ok(facilitatorService.getAllMembers());
    }

    // ✅ Search member by National ID
    @GetMapping("/members/search/{nationalId}")
    public ResponseEntity<MemberDetailsDTO> getMemberByNationalId(@PathVariable String nationalId) {
        return ResponseEntity.ok(facilitatorService.getMemberByNationalId(nationalId));
    }
}

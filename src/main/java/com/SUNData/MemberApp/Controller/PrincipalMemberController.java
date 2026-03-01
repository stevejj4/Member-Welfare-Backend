package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.*;
import com.SUNData.MemberApp.Service.PrincipalMemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class PrincipalMemberController {

    private final PrincipalMemberService principalMemberService;

    public PrincipalMemberController(PrincipalMemberService principalMemberService) {
        this.principalMemberService = principalMemberService;
    }

    // Register Principal + Mandatory NextOfKin + Optional Dependants
    @PostMapping("/register")
    public ResponseEntity<MemberDetailsDTO> registerMember(
            @Valid @RequestBody RegisterMemberRequestDTO request) {

        MemberDetailsDTO dto = principalMemberService.registerFullMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // Get Full Member Details
    @GetMapping("/{id}")
    public ResponseEntity<MemberDetailsDTO> getMemberDetails(@PathVariable Long id) {
        return ResponseEntity.ok(principalMemberService.getFullMemberDetails(id));
    }

    // Update Principal Only
    @PutMapping("/{id}")
    public ResponseEntity<PrincipalMemberDTO> updatePrincipal(
            @PathVariable Long id,
            @Valid @RequestBody PrincipalMemberDTO dto) {

        return ResponseEntity.ok(principalMemberService.updatePrincipal(id, dto));
    }

    // Delete Principal (cascade children)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrincipal(@PathVariable Long id) {
        principalMemberService.deletePrincipal(id);
        return ResponseEntity.noContent().build();
    }
}
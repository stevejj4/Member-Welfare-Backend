package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.*;
import com.SUNData.MemberApp.Service.PrincipalMemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/members")
public class PrincipalMemberController {

    private final PrincipalMemberService principalMemberService;

    public PrincipalMemberController(PrincipalMemberService principalMemberService) {
        this.principalMemberService = principalMemberService;
    }

    // ---------------- Principal Member Endpoints ----------------

    /**
     * Register a new Principal Member aggregate:
     * - Principal Member (mandatory)
     * - Next of Kin (mandatory)
     * - Dependants (optional, max 6)
     */
    @PostMapping("/register")
    public ResponseEntity<MemberDetailsDTO> registerMember(
            @Valid @RequestBody RegisterMemberRequestDTO request) {
        MemberDetailsDTO dto = principalMemberService.registerFullMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Get full details of a Principal Member,
     * including Next of Kin and Dependants.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberDetailsDTO> getMemberDetails(@PathVariable Long id) {
        return ResponseEntity.ok(principalMemberService.getFullMemberDetails(id));
    }

    /** When user wants to update full details of Principal Member */
    @PutMapping("/{id}")
    public ResponseEntity<PrincipalMemberDTO> updatePrincipal(
            @PathVariable Long id,
            @Valid @RequestBody PrincipalMemberDTO dto) {
        return ResponseEntity.ok(principalMemberService.updatePrincipal(id, dto));
    }

    /** Partial update of Principal Member (PATCH-style) */
    @PatchMapping("/{id}")
    public ResponseEntity<PrincipalMemberDTO> patchPrincipal(
            @PathVariable Long id,
            @RequestBody PrincipalMemberDTO dto) {
        return ResponseEntity.ok(principalMemberService.patchPrincipal(id, dto));
    }

    /** Delete Principal Member (cascade deletes Next of Kin + Dependants) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrincipal(@PathVariable Long id) {
        principalMemberService.deletePrincipal(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- Next of Kin Endpoints ----------------

    /** Full update of Next of Kin (PUT-style) */
    @PutMapping("/{principalId}/next-of-kin")
    public ResponseEntity<NextOfKinDTO> updateNextOfKin(
            @PathVariable Long principalId,
            @Valid @RequestBody NextOfKinDTO dto) {
        return ResponseEntity.ok(principalMemberService.updateNextOfKin(principalId, dto));
    }

    /** Partial update of Next of Kin (PATCH-style) */
    @PatchMapping("/{principalId}/next-of-kin")
    public ResponseEntity<NextOfKinDTO> patchNextOfKin(
            @PathVariable Long principalId,
            @RequestBody NextOfKinDTO dto) {
        return ResponseEntity.ok(principalMemberService.patchNextOfKin(principalId, dto));
    }

    // ---------------- Dependant Endpoints ----------------

    /** Add a new dependant to a Principal Member (max 6 allowed) */
    @PostMapping("/{principalId}/dependants")
    public ResponseEntity<DependantDTO> addDependant(
            @PathVariable Long principalId,
            @Valid @RequestBody DependantDTO dto) {
        DependantDTO saved = principalMemberService.addDependant(principalId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /** Partial update of a dependant (PATCH-style) */
    @PatchMapping("/dependants/{dependantId}")
    public ResponseEntity<DependantDTO> patchDependant(
            @PathVariable Long dependantId,
            @RequestBody DependantDTO dto) {
        return ResponseEntity.ok(principalMemberService.patchDependant(dependantId, dto));
    }

    /**
     * Delete a dependant through its Principal Member.
     * Ensures dependants cannot be deleted independently.
     */
    @DeleteMapping("/{principalId}/dependants/{dependantId}")
    public ResponseEntity<Void> deleteDependant(
            @PathVariable Long principalId,
            @PathVariable Long dependantId) {
        principalMemberService.deleteDependant(principalId, dependantId);
        return ResponseEntity.noContent().build();
    }
}
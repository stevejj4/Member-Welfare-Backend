package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Member.*;
import com.SUNData.MemberApp.Service.member.CoordinatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coordinator")
public class CoordinatorController {

    private final CoordinatorService coordinatorService;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    // Register member
    @PostMapping("/members/register")
    public ResponseEntity<MemberDetailsDTO> registerMember(@RequestBody RegisterMemberRequestDTO request) {
        return ResponseEntity.ok(coordinatorService.registerMember(request));
    }

    // ✅ View all members
    @GetMapping("/members")
    public ResponseEntity<List<MemberDetailsDTO>> getAllMembers() {
        return ResponseEntity.ok(coordinatorService.getAllMembers());
    }
    // ✅ Get member by ID
    @GetMapping("/members/{id}")
    public ResponseEntity<MemberDetailsDTO> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(coordinatorService.getMemberById(id));
    }
    // ✅ Get member by National ID
    @GetMapping("/members/national-id/{nationalId}")
    public ResponseEntity<MemberDetailsDTO> getMemberByNationalId(@PathVariable String nationalId) {
        return ResponseEntity.ok(coordinatorService.getMemberByNationalId(nationalId));
    }

    // ✅ Update Next of Kin
    @PutMapping("/members/{principalId}/next-of-kin")
    public ResponseEntity<NextOfKinDTO> updateNextOfKin(@PathVariable Long principalId,
                                                        @RequestBody NextOfKinDTO dto) {
        return ResponseEntity.ok(coordinatorService.updateNextOfKin(principalId, dto));
    }

    // ✅ Patch Next of Kin
    @PatchMapping("/members/{principalId}/next-of-kin")
    public ResponseEntity<NextOfKinDTO> patchNextOfKin(@PathVariable Long principalId,
                                                       @RequestBody NextOfKinDTO dto) {
        return ResponseEntity.ok(coordinatorService.patchNextOfKin(principalId, dto));
    }
    @DeleteMapping("/members/{principalId}/next-of-kin")
    public ResponseEntity<Void> deleteNextOfKin(@PathVariable Long principalId) {
        coordinatorService.deleteNextOfKin(principalId);
        return ResponseEntity.noContent().build();
    }


    // ✅ Patch Dependant
    @PatchMapping("/members/dependants/{dependantId}")
    public ResponseEntity<DependantDTO> patchDependant(@PathVariable Long dependantId,
                                                       @RequestBody DependantDTO dto) {
        return ResponseEntity.ok(coordinatorService.patchDependant(dependantId, dto));
    }

    // ✅ Delete dependant
    @DeleteMapping("/{principalId}/dependants/{dependantId}")
    public ResponseEntity<Void> deleteDependant(@PathVariable Long principalId,
                                                @PathVariable Long dependantId) {
        coordinatorService.deleteDependant(principalId, dependantId);
        return ResponseEntity.noContent().build();
    }
}

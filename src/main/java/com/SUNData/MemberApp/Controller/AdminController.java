package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Member.*;
import com.SUNData.MemberApp.Service.admin.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ---------------- MEMBER MANAGEMENT ----------------

    // ✅ View all members
    @GetMapping("/members")
    public ResponseEntity<List<MemberDetailsDTO>> getAllMembers() {
        return ResponseEntity.ok(adminService.getAllMembers());
    }

    // ✅ Get member by ID
    @GetMapping("/members/{id}")
    public ResponseEntity<MemberDetailsDTO> getMemberDetails(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getFullMemberDetails(id));
    }

    // ✅ Get member by National ID
    @GetMapping("/members/search/{nationalId}")
    public ResponseEntity<MemberDetailsDTO> getMemberByNationalId(@PathVariable String nationalId) {
        return ResponseEntity.ok(adminService.getFullMemberDetailsByNationalId(nationalId));
    }

    // ✅ Register new member
    @PostMapping("/members/register")
    public ResponseEntity<MemberDetailsDTO> registerMember(@RequestBody RegisterMemberRequestDTO request) {
        return ResponseEntity.ok(adminService.registerFullMember(request));
    }

    // ✅ Update principal member
    @PutMapping("/members/{id}")
    public ResponseEntity<PrincipalMemberDTO> updatePrincipal(@PathVariable Long id,
                                                              @RequestBody PrincipalMemberDTO dto) {
        return ResponseEntity.ok(adminService.updatePrincipal(id, dto));
    }

    // ✅ Patch principal member
    @PatchMapping("/members/{id}")
    public ResponseEntity<PrincipalMemberDTO> patchPrincipal(@PathVariable Long id,
                                                             @RequestBody PrincipalMemberDTO dto) {
        return ResponseEntity.ok(adminService.patchPrincipal(id, dto));
    }

    // ✅ Update Next of Kin
    @PutMapping("/members/{principalId}/next-of-kin")
    public ResponseEntity<NextOfKinDTO> updateNextOfKin(@PathVariable Long principalId,
                                                        @RequestBody NextOfKinDTO dto) {
        return ResponseEntity.ok(adminService.updateNextOfKin(principalId, dto));
    }

    // ✅ Patch Next of Kin
    @PatchMapping("/members/{principalId}/next-of-kin")
    public ResponseEntity<NextOfKinDTO> patchNextOfKin(@PathVariable Long principalId,
                                                       @RequestBody NextOfKinDTO dto) {
        return ResponseEntity.ok(adminService.patchNextOfKin(principalId, dto));
    }

    @DeleteMapping("/members/{principalId}/next-of-kin")
    public ResponseEntity<Void> deleteNextOfKin(@PathVariable Long principalId) {
        adminService.deleteNextOfKin(principalId);
        return ResponseEntity.noContent().build();
    }

    // ✅ Add dependant
    @PostMapping("/members/{principalId}/dependants")
    public ResponseEntity<DependantDTO> addDependant(@PathVariable Long principalId,
                                                     @RequestBody DependantDTO dto) {
        return ResponseEntity.ok(adminService.addDependant(principalId, dto));
    }

    // ✅ Patch dependant
    @PatchMapping("/members/dependants/{dependantId}")
    public ResponseEntity<DependantDTO> patchDependant(@PathVariable Long dependantId,
                                                       @RequestBody DependantDTO dto) {
        return ResponseEntity.ok(adminService.patchDependant(dependantId, dto));
    }

    // ✅ Delete principal member
    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deletePrincipal(@PathVariable Long id) {
        adminService.deletePrincipal(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Delete dependant
    @DeleteMapping("/members/{principalId}/dependants/{dependantId}")
    public ResponseEntity<Void> deleteDependant(@PathVariable Long principalId,
                                                @PathVariable Long dependantId) {
        adminService.deleteDependant(principalId, dependantId);
        return ResponseEntity.noContent().build();
    }
}

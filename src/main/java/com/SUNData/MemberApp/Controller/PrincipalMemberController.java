package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.MemberDetailsDTO;
import com.SUNData.MemberApp.DTOs.NextOfKinDTO;
import com.SUNData.MemberApp.DTOs.PrincipalMemberDTO;
import com.SUNData.MemberApp.Model.NextOfKinModel;
import com.SUNData.MemberApp.Model.PrincipalMemberModel;
import com.SUNData.MemberApp.Service.NextOfKinService;
import com.SUNData.MemberApp.Service.PrincipalMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PrincipalMemberController {

    private final PrincipalMemberService principalMemberService;
    private final NextOfKinService nextOfKinService;

    public PrincipalMemberController(PrincipalMemberService principalMemberService, NextOfKinService nextOfKinService) {
        this.principalMemberService = principalMemberService;
        this.nextOfKinService = nextOfKinService;
    }

    // GET all members
    @GetMapping
    public List<PrincipalMemberModel> getAllMembers() {
        return principalMemberService.getAllMembers();
    }

    // GET member details by ID

    @GetMapping("/{id}/details")
    public ResponseEntity<MemberDetailsDTO> getMemberDetails(@PathVariable Long id) {
        PrincipalMemberModel member = principalMemberService.getMemberById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        List<NextOfKinDTO> kinDTOs = nextOfKinService.getNextOfKinByPrincipalMember(id)
                .stream()
                .map(NextOfKinDTO::new)
                .toList();

        MemberDetailsDTO dto = new MemberDetailsDTO(new PrincipalMemberDTO(member), kinDTOs);
        return ResponseEntity.ok(dto);
    }

    // POST register new member
    @PostMapping("/register")
    public ResponseEntity<PrincipalMemberModel> registerMember(@RequestBody PrincipalMemberModel memberData) {
        PrincipalMemberModel saved = principalMemberService.addMember(memberData);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT update member
    @PutMapping("/{id}")
    public ResponseEntity<PrincipalMemberModel> updateMember(@PathVariable Long id,
                                                             @RequestBody PrincipalMemberModel updated) {
        PrincipalMemberModel saved = principalMemberService.updateMember(id, updated);
        return ResponseEntity.ok(saved);
    }

    // DELETE member
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        principalMemberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
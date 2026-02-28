package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.DependantDTO;
import com.SUNData.MemberApp.DTOs.MemberDetailsDTO;
import com.SUNData.MemberApp.DTOs.NextOfKinDTO;
import com.SUNData.MemberApp.DTOs.PrincipalMemberDTO;
import com.SUNData.MemberApp.Model.DependantModel;
import com.SUNData.MemberApp.Model.NextOfKinModel;
import com.SUNData.MemberApp.Model.PrincipalMemberModel;
import com.SUNData.MemberApp.Service.DependantService;
import com.SUNData.MemberApp.Service.NextOfKinService;
import com.SUNData.MemberApp.Service.PrincipalMemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class PrincipalMemberController {

    private final PrincipalMemberService principalMemberService;
    private final NextOfKinService nextOfKinService;
    private final DependantService dependantService;

    public PrincipalMemberController(PrincipalMemberService principalMemberService, NextOfKinService nextOfKinService, DependantService dependantService) {
        this.principalMemberService = principalMemberService;
        this.nextOfKinService = nextOfKinService;
        this.dependantService = dependantService;
    }

    // GET all members
    @GetMapping
    public List<PrincipalMemberModel> getAllMembers() {
        return principalMemberService.getAllMembers();
    }

    // GET member details by ID
    //  makes three separate database calls -- PM, Nok & Dept
    // used DTO to resent what is important and data privacy

    @GetMapping("/{id}")
    public ResponseEntity<MemberDetailsDTO> getMemberDetails(@PathVariable Long id) {
        PrincipalMemberModel member = principalMemberService.getMemberById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        List<NextOfKinDTO> kinDTOs = nextOfKinService.getNextOfKinByPrincipalMember(id)
                .stream().map(NextOfKinDTO::new).toList();

        List<DependantDTO> dependantDTOs = dependantService.getDependantsByPrincipalMember(id)
                .stream().map(DependantDTO::new).toList();

        MemberDetailsDTO dto = new MemberDetailsDTO(new PrincipalMemberDTO(member), kinDTOs, dependantDTOs);
        return ResponseEntity.ok(dto);
    }


    // POST register new member
    @PostMapping("/register")
    public ResponseEntity<PrincipalMemberModel> registerMember(@RequestBody PrincipalMemberModel memberData) {
        PrincipalMemberModel saved = principalMemberService.addMember(memberData);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT update member
    @PutMapping("/{id}/edit")
    public ResponseEntity<PrincipalMemberModel> updateMember(@PathVariable Long id,
                                                             @RequestBody PrincipalMemberModel updated) {
        PrincipalMemberModel saved = principalMemberService.updateMember(id, updated);
        return ResponseEntity.ok(saved);
    }

    // DELETE member
    @DeleteMapping("/{id}/deleting")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        principalMemberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{memberId}/dependants")
    public ResponseEntity<DependantDTO> addDependant(@PathVariable Long memberId,
                                                     @Valid @RequestBody DependantModel dependant) {
        DependantModel saved = dependantService.addDependant(memberId, dependant);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DependantDTO(saved));
    }

    @PutMapping("/{memberId}/dependants/{dependantId}")
    public ResponseEntity<DependantDTO> updateDependant(@PathVariable Long memberId,
                                                        @PathVariable Long dependantId,
                                                        @Valid @RequestBody DependantModel dependant) {
        DependantModel updated = dependantService.updateDependant(dependantId, dependant);
        return ResponseEntity.ok(new DependantDTO(updated));
    }

    @DeleteMapping("/{memberId}/dependants/{dependantId}")
    public ResponseEntity<Void> deleteDependant(@PathVariable Long memberId,
                                                @PathVariable Long dependantId) {
        dependantService.deleteDependant(dependantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{memberId}/dependants")
    public ResponseEntity<List<DependantDTO>> getDependants(@PathVariable Long memberId) {
        List<DependantDTO> dependants = dependantService.getDependantsByPrincipalMember(memberId)
                .stream().map(DependantDTO::new).toList();
        return ResponseEntity.ok(dependants);
    }

}
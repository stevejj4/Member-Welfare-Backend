package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.Model.PrincipalMemberModel;
import com.SUNData.MemberApp.Service.PrincipalMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
public class PrincipalMemberController {

    private final PrincipalMemberService principalMemberService;

    public PrincipalMemberController(PrincipalMemberService principalMemberService) {
        this.principalMemberService = principalMemberService;
    }

    // GET all members
    @GetMapping
    public List<PrincipalMemberModel> getAllMembers() {
        return principalMemberService.getAllMembers();
    }

    // GET member by ID
    @GetMapping("/{id}")
    public ResponseEntity<PrincipalMemberModel> getMemberById(@PathVariable Long id) {
        return principalMemberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST register new member
    @PostMapping
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
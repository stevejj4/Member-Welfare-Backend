package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.Model.PrincipalMemberModel;
import com.SUNData.MemberApp.Service.PrincipalMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PrincipalMemberController {

    private final PrincipalMemberService principalMemberService;

    public PrincipalMemberController(PrincipalMemberService principalMemberService) {
        this.principalMemberService = principalMemberService;
    }

    //GET /api/members
    @GetMapping("/member")

    public List<PrincipalMemberModel> getAllMembers() {
        return principalMemberService.getAllMembers();
    }
    //@GetMapping("/member{id}")

    // POST /api/register
    @PostMapping("/register")
    public ResponseEntity<PrincipalMemberModel> registerMember(@RequestBody PrincipalMemberModel memberData) {
        PrincipalMemberModel saved = principalMemberService.addMember(memberData);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

}

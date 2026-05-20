package com.SUNData.MemberApp.Service.member;

import com.SUNData.MemberApp.DTOs.Member.*;
import com.SUNData.MemberApp.Service.admin.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacilitatorService {

    private final AdminService adminService;

    public FacilitatorService(AdminService adminService) {
        this.adminService = adminService;
    }

    // ✅ Register new member (Principal + Next of Kin + optional Dependants)
    public MemberDetailsDTO registerMember(RegisterMemberRequestDTO request) {
        return adminService.registerFullMember(request);
    }

    // ✅ Edit principal member details only
    public PrincipalMemberDTO updatePrincipal(Long id, PrincipalMemberDTO dto) {
        return adminService.updatePrincipal(id, dto);
    }

    public PrincipalMemberDTO patchPrincipal(Long id, PrincipalMemberDTO dto) {
        return adminService.patchPrincipal(id, dto);
    }

    // ✅ View all members
    public List<MemberDetailsDTO> getAllMembers() {
        return adminService.getAllMembers();
    }

    // ✅ Search member by National ID
    public MemberDetailsDTO getMemberByNationalId(String nationalId) {
        return adminService.getFullMemberDetailsByNationalId(nationalId);
    }
}

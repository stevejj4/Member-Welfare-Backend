package com.SUNData.MemberApp.Service.member;

import com.SUNData.MemberApp.DTOs.Member.*;
import com.SUNData.MemberApp.Service.admin.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final AdminService adminService;

    public MemberService(AdminService adminService) {
        this.adminService = adminService;
    }

    public MemberDetailsDTO registerMember(RegisterMemberRequestDTO request) {
        return adminService.registerFullMember(request);
    }

    public List<MemberDetailsDTO> getAllMembers() {
        return adminService.getAllMembers();
    }

    public MemberDetailsDTO getMemberById(Long id) {
        return adminService.getFullMemberDetails(id);
    }

    public MemberDetailsDTO getMemberByNationalId(String nationalId) {
        return adminService.getFullMemberDetailsByNationalId(nationalId);
    }

    public PrincipalMemberDTO updatePrincipal(Long id, PrincipalMemberDTO dto) {
        return adminService.updatePrincipal(id, dto);
    }

    public PrincipalMemberDTO patchPrincipal(Long id, PrincipalMemberDTO dto) {
        return adminService.patchPrincipal(id, dto);
    }

    public NextOfKinDTO updateNextOfKin(Long principalId, NextOfKinDTO dto) {
        return adminService.updateNextOfKin(principalId, dto);
    }

    public NextOfKinDTO patchNextOfKin(Long principalId, NextOfKinDTO dto) {
        return adminService.patchNextOfKin(principalId, dto);
    }

    public void deleteNextOfKin(Long principalId) {
        adminService.deleteNextOfKin(principalId);
    }

    public DependantDTO addDependant(Long principalId, DependantDTO dto) {
        return adminService.addDependant(principalId, dto);
    }

    public DependantDTO patchDependant(Long dependantId, DependantDTO dto) {
        return adminService.patchDependant(dependantId, dto);
    }

    public void deleteDependant(Long principalId, Long dependantId) {
        adminService.deleteDependant(principalId, dependantId);
    }
}

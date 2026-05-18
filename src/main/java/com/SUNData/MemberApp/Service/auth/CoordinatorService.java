package com.SUNData.MemberApp.Service.auth;

import com.SUNData.MemberApp.DTOs.Member.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoordinatorService {

    private final AdminService adminService;

    public CoordinatorService(AdminService adminService) {
        this.adminService = adminService;
    }

    // ✅ Register new member
    public MemberDetailsDTO registerMember(RegisterMemberRequestDTO request) {
        return adminService.registerFullMember(request);
    }

    // ✅ View all members
    public List<MemberDetailsDTO> getAllMembers() {
        return adminService.getAllMembers();
    }

    // ✅ Get member by ID
    public MemberDetailsDTO getMemberById(Long id) {
        return adminService.getFullMemberDetails(id);
    }

    // ✅ Get member by National ID
    public MemberDetailsDTO getMemberByNationalId(String nationalId) {
        return adminService.getFullMemberDetailsByNationalId(nationalId);
    }

    // ✅ Edit Next of Kin (full update)
    public NextOfKinDTO updateNextOfKin(Long principalId, NextOfKinDTO dto) {
        return adminService.updateNextOfKin(principalId, dto);
    }

    // ✅ Edit Next of Kin (partial update)
    public NextOfKinDTO patchNextOfKin(Long principalId, NextOfKinDTO dto) {
        return adminService.patchNextOfKin(principalId, dto);
    }


    // ✅ Add dependant
    public DependantDTO addDependant(Long principalId, DependantDTO dto) {
        return adminService.addDependant(principalId, dto);
    }

    // ✅ Edit Dependant (partial update)
    public DependantDTO patchDependant(Long dependantId, DependantDTO dto) {
        return adminService.patchDependant(dependantId, dto);
    }

    // ✅ Delete dependant
    public void deleteDependant(Long principalId, Long dependantId) {
        adminService.deleteDependant(principalId, dependantId);
    }
}
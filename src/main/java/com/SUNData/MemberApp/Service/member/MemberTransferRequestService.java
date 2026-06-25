package com.SUNData.MemberApp.Service.member;

import com.SUNData.MemberApp.DTOs.Member.CreateMemberTransferRequestDTO;
import com.SUNData.MemberApp.DTOs.Member.ApproveMemberTransferRequestDTO;
import com.SUNData.MemberApp.DTOs.Member.MemberDetailsDTO;
import com.SUNData.MemberApp.DTOs.Member.MemberTransferRequestDTO;
import com.SUNData.MemberApp.DTOs.Member.TransferMemberRequestDTO;
import com.SUNData.MemberApp.Enums.RegistrationType;
import com.SUNData.MemberApp.Enums.TransferRequestStatus;
import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Exceptions.ResourceNotFoundException;
import com.SUNData.MemberApp.Exceptions.ValidationException;
import com.SUNData.MemberApp.Model.GroupModel.MemberGroupModel;
import com.SUNData.MemberApp.Model.LocationModel.CountyModel;
import com.SUNData.MemberApp.Model.LocationModel.SubCountyModel;
import com.SUNData.MemberApp.Model.LocationModel.WardModel;
import com.SUNData.MemberApp.Model.MemberModel.MemberTransferRequestModel;
import com.SUNData.MemberApp.Model.MemberModel.PrincipalMemberModel;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.*;
import com.SUNData.MemberApp.Service.group.GroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MemberTransferRequestService {
    private final MemberTransferRequestRepository transferRepository;
    private final PrincipalMemberRepository principalRepository;
    private final CountyRepository countyRepository;
    private final SubCountyRepository subCountyRepository;
    private final WardRepository wardRepository;
    private final MemberGroupRepository groupRepository;
    private final GroupService groupService;
    private final MemberService memberService;

    public MemberTransferRequestService(
            MemberTransferRequestRepository transferRepository,
            PrincipalMemberRepository principalRepository,
            CountyRepository countyRepository,
            SubCountyRepository subCountyRepository,
            WardRepository wardRepository,
            MemberGroupRepository groupRepository,
            GroupService groupService,
            MemberService memberService) {
        this.transferRepository = transferRepository;
        this.principalRepository = principalRepository;
        this.countyRepository = countyRepository;
        this.subCountyRepository = subCountyRepository;
        this.wardRepository = wardRepository;
        this.groupRepository = groupRepository;
        this.groupService = groupService;
        this.memberService = memberService;
    }

    @Transactional
    public MemberTransferRequestDTO createRequest(Long memberId, CreateMemberTransferRequestDTO request) {
        PrincipalMemberModel member = principalRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Principal Member not found: " + memberId));
        if (member.getWard() != null) {
            groupService.validateUserCanAccessWard(groupService.currentUser(), member.getWard());
        }
        CountyModel county = countyRepository.findById(request.getCountyId())
                .orElseThrow(() -> new ValidationException("County not found"));
        SubCountyModel subCounty = subCountyRepository.findById(request.getSubCountyId())
                .orElseThrow(() -> new ValidationException("Sub-county not found"));
        WardModel ward = wardRepository.findById(request.getWardId())
                .orElseThrow(() -> new ValidationException("Ward not found"));

        groupService.validateLocationHierarchy(county, subCounty, ward);

        MemberTransferRequestModel model = new MemberTransferRequestModel();
        model.setPrincipalMember(member);
        model.setTargetCounty(county);
        model.setTargetSubCounty(subCounty);
        model.setTargetWard(ward);
        model.setRegistrationType(RegistrationType.INDIVIDUAL);
        model.setReason(request.getReason().trim());
        model.setRequestedBy(groupService.currentUser());
        return new MemberTransferRequestDTO(transferRepository.save(model));
    }

    @Transactional(readOnly = true)
    public List<MemberTransferRequestDTO> getHistory() {
        SystemUserModel user = groupService.currentUser();
        List<MemberTransferRequestModel> rows;

        if (user.getRole() == UserRole.ADMIN) {
            rows = transferRepository.findAllByOrderByRequestedAtDesc();
        } else if (user.getRole() == UserRole.COORDINATOR) {
            if (user.getAssignedSubCounty() == null) {
                throw new ValidationException("Your account has no assigned sub-county");
            }
            rows = transferRepository.findByTargetSubCounty_IdOrderByRequestedAtDesc(
                    user.getAssignedSubCounty().getId()
            );
        } else {
            if (user.getAssignedWards() == null || user.getAssignedWards().isEmpty()) {
                throw new ValidationException("Your account has no assigned wards");
            }
            Set<Long> wardIds = user.getAssignedWards().stream()
                    .map(WardModel::getId)
                    .collect(Collectors.toSet());
            rows = transferRepository.findByTargetWard_IdInOrderByRequestedAtDesc(wardIds);
        }

        return rows.stream().map(MemberTransferRequestDTO::new).toList();
    }

    @Transactional
    public MemberDetailsDTO approve(Long requestId, ApproveMemberTransferRequestDTO approval) {
        MemberTransferRequestModel request = transferRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer request not found"));
        if (request.getStatus() != TransferRequestStatus.PENDING) {
            throw new ValidationException("Transfer request has already been processed");
        }

        MemberGroupModel targetGroup = groupService.getAccessibleGroupOrThrow(
                approval.getGroupId(),
                request.getTargetWard().getId()
        );

        TransferMemberRequestDTO transfer = new TransferMemberRequestDTO();
        transfer.setWardId(request.getTargetWard().getId());
        transfer.setRegistrationType(RegistrationType.GROUP);
        transfer.setGroupId(targetGroup.getId());

        MemberDetailsDTO result = memberService.transferMember(request.getPrincipalMember().getId(), transfer);
        request.setTargetGroup(targetGroup);
        request.setRegistrationType(RegistrationType.GROUP);
        request.setStatus(TransferRequestStatus.APPROVED);
        request.setApprovedBy(groupService.currentUser());
        request.setApprovedAt(LocalDateTime.now());
        transferRepository.save(request);
        return result;
    }
}

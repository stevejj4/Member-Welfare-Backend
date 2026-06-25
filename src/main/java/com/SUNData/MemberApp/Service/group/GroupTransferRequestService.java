package com.SUNData.MemberApp.Service.group;

import com.SUNData.MemberApp.DTOs.Group.CreateGroupTransferRequestDTO;
import com.SUNData.MemberApp.DTOs.Group.GroupTransferRequestDTO;
import com.SUNData.MemberApp.DTOs.Group.MemberGroupDTO;
import com.SUNData.MemberApp.Enums.TransferRequestStatus;
import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Exceptions.ResourceNotFoundException;
import com.SUNData.MemberApp.Exceptions.ValidationException;
import com.SUNData.MemberApp.Model.GroupModel.GroupTransferRequestModel;
import com.SUNData.MemberApp.Model.GroupModel.MemberGroupModel;
import com.SUNData.MemberApp.Model.LocationModel.CountyModel;
import com.SUNData.MemberApp.Model.LocationModel.SubCountyModel;
import com.SUNData.MemberApp.Model.LocationModel.WardModel;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupTransferRequestService {
    private final GroupTransferRequestRepository transferRepository;
    private final MemberGroupRepository groupRepository;
    private final CountyRepository countyRepository;
    private final SubCountyRepository subCountyRepository;
    private final WardRepository wardRepository;
    private final GroupService groupService;

    public GroupTransferRequestService(
            GroupTransferRequestRepository transferRepository,
            MemberGroupRepository groupRepository,
            CountyRepository countyRepository,
            SubCountyRepository subCountyRepository,
            WardRepository wardRepository,
            GroupService groupService) {
        this.transferRepository = transferRepository;
        this.groupRepository = groupRepository;
        this.countyRepository = countyRepository;
        this.subCountyRepository = subCountyRepository;
        this.wardRepository = wardRepository;
        this.groupService = groupService;
    }

    @Transactional
    public GroupTransferRequestDTO createRequest(Long groupId, CreateGroupTransferRequestDTO request) {
        MemberGroupModel group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        CountyModel county = countyRepository.findById(request.getCountyId())
                .orElseThrow(() -> new ValidationException("County not found"));
        SubCountyModel subCounty = subCountyRepository.findById(request.getSubCountyId())
                .orElseThrow(() -> new ValidationException("Sub-county not found"));
        WardModel ward = wardRepository.findById(request.getWardId())
                .orElseThrow(() -> new ValidationException("Ward not found"));

        groupService.validateUserCanAccessWard(groupService.currentUser(), group.getWard());
        groupService.validateLocationHierarchy(county, subCounty, ward);

        GroupTransferRequestModel model = new GroupTransferRequestModel();
        model.setGroup(group);
        model.setTargetCounty(county);
        model.setTargetSubCounty(subCounty);
        model.setTargetWard(ward);
        model.setReason(request.getReason().trim());
        model.setRequestedBy(groupService.currentUser());
        return new GroupTransferRequestDTO(transferRepository.save(model));
    }

    @Transactional(readOnly = true)
    public List<GroupTransferRequestDTO> getHistory() {
        SystemUserModel user = groupService.currentUser();
        List<GroupTransferRequestModel> rows;

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

        return rows.stream().map(GroupTransferRequestDTO::new).toList();
    }

    @Transactional
    public MemberGroupDTO approve(Long requestId) {
        GroupTransferRequestModel request = transferRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Group transfer request not found"));
        if (request.getStatus() != TransferRequestStatus.PENDING) {
            throw new ValidationException("Group transfer request has already been processed");
        }

        groupService.validateUserCanAccessWard(groupService.currentUser(), request.getTargetWard());

        MemberGroupModel group = request.getGroup();
        group.setCounty(request.getTargetCounty());
        group.setSubCounty(request.getTargetSubCounty());
        group.setWard(request.getTargetWard());
        MemberGroupModel saved = groupRepository.save(group);

        request.setStatus(TransferRequestStatus.APPROVED);
        request.setApprovedBy(groupService.currentUser());
        request.setApprovedAt(LocalDateTime.now());
        transferRepository.save(request);
        return new MemberGroupDTO(saved);
    }
}

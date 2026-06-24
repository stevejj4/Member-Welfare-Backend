package com.SUNData.MemberApp.Service.group;

import com.SUNData.MemberApp.DTOs.Group.CreateGroupRequestDTO;
import com.SUNData.MemberApp.DTOs.Group.GroupDetailsDTO;
import com.SUNData.MemberApp.DTOs.Group.MemberGroupDTO;
import com.SUNData.MemberApp.DTOs.Member.PrincipalMemberDTO;
import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Exceptions.ResourceNotFoundException;
import com.SUNData.MemberApp.Exceptions.ValidationException;
import com.SUNData.MemberApp.Model.GroupModel.MemberGroupModel;
import com.SUNData.MemberApp.Model.LocationModel.CountyModel;
import com.SUNData.MemberApp.Model.LocationModel.SubCountyModel;
import com.SUNData.MemberApp.Model.LocationModel.WardModel;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.CountyRepository;
import com.SUNData.MemberApp.Repository.MemberGroupRepository;
import com.SUNData.MemberApp.Repository.PrincipalMemberRepository;
import com.SUNData.MemberApp.Repository.SubCountyRepository;
import com.SUNData.MemberApp.Repository.SystemUserRepository;
import com.SUNData.MemberApp.Repository.WardRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class GroupService {

    private final MemberGroupRepository groupRepository;
    private final PrincipalMemberRepository principalMemberRepository;
    private final CountyRepository countyRepository;
    private final SubCountyRepository subCountyRepository;
    private final WardRepository wardRepository;
    private final SystemUserRepository userRepository;

    public GroupService(
            MemberGroupRepository groupRepository,
            PrincipalMemberRepository principalMemberRepository,
            CountyRepository countyRepository,
            SubCountyRepository subCountyRepository,
            WardRepository wardRepository,
            SystemUserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.principalMemberRepository = principalMemberRepository;
        this.countyRepository = countyRepository;
        this.subCountyRepository = subCountyRepository;
        this.wardRepository = wardRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MemberGroupDTO createGroup(CreateGroupRequestDTO request) {
        SystemUserModel user = currentUser();
        CountyModel county = countyRepository.findById(request.getCountyId())
                .orElseThrow(() -> new ValidationException("County not found"));
        SubCountyModel subCounty = subCountyRepository.findById(request.getSubCountyId())
                .orElseThrow(() -> new ValidationException("Sub-county not found"));
        WardModel ward = wardRepository.findById(request.getWardId())
                .orElseThrow(() -> new ValidationException("Ward not found"));

        validateLocationHierarchy(county, subCounty, ward);
        validateUserCanAccessWard(user, ward);

        MemberGroupModel group = new MemberGroupModel();
        group.setName(request.getName().trim());
        group.setCounty(county);
        group.setSubCounty(subCounty);
        group.setWard(ward);
        group.setCreatedBy(user);

        MemberGroupModel saved = groupRepository.save(group);
        saved.setGroupId(String.format("GRP-%06d", saved.getId()));
        return new MemberGroupDTO(groupRepository.save(saved));
    }

    @Transactional(readOnly = true)
    public List<MemberGroupDTO> getGroups(Long wardId) {
        SystemUserModel user = currentUser();

        if (wardId != null) {
            WardModel ward = wardRepository.findById(wardId)
                    .orElseThrow(() -> new ValidationException("Ward not found"));
            validateUserCanAccessWard(user, ward);
            return groupRepository.findByWardId(wardId).stream()
                    .map(MemberGroupDTO::new)
                    .toList();
        }

        if (user.getRole() == UserRole.ADMIN) {
            return groupRepository.findAll().stream().map(MemberGroupDTO::new).toList();
        }

        if (user.getRole() == UserRole.COORDINATOR) {
            ensureSubCountyAssigned(user);
            return groupRepository.findBySubCountyId(user.getAssignedSubCounty().getId()).stream()
                    .map(MemberGroupDTO::new)
                    .toList();
        }

        ensureWardsAssigned(user);
        Set<Long> wardIds = user.getAssignedWards().stream()
                .map(WardModel::getId)
                .collect(java.util.stream.Collectors.toSet());
        return groupRepository.findByWardIdIn(wardIds).stream()
                .map(MemberGroupDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberGroupModel getAccessibleGroupOrThrow(Long groupId, Long wardId) {
        MemberGroupModel group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        if (!group.getWard().getId().equals(wardId)) {
            throw new ValidationException("Selected group does not belong to the selected ward");
        }

        validateUserCanAccessWard(currentUser(), group.getWard());
        return group;
    }

    @Transactional(readOnly = true)
    public GroupDetailsDTO getGroupDetails(Long groupId) {
        MemberGroupModel group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        validateUserCanAccessWard(currentUser(), group.getWard());

        List<PrincipalMemberDTO> members = principalMemberRepository.findByGroupId(groupId).stream()
                .map(PrincipalMemberDTO::new)
                .toList();

        return new GroupDetailsDTO(new MemberGroupDTO(group), members);
    }

    public void validateUserCanAccessWard(SystemUserModel user, WardModel ward) {
        if (user.getRole() == UserRole.ADMIN) {
            return;
        }

        if (user.getRole() == UserRole.COORDINATOR) {
            ensureSubCountyAssigned(user);
            if (!ward.getSubCounty().getId().equals(user.getAssignedSubCounty().getId())) {
                throw new AccessDeniedException("You can only access wards in your assigned sub-county");
            }
            return;
        }

        if (user.getRole() == UserRole.FACILITATOR) {
            ensureWardsAssigned(user);
            boolean allowed = user.getAssignedWards().stream()
                    .anyMatch(assignedWard -> assignedWard.getId().equals(ward.getId()));
            if (!allowed) {
                throw new AccessDeniedException("You can only access your assigned ward(s)");
            }
        }
    }

    public void validateLocationHierarchy(CountyModel county, SubCountyModel subCounty, WardModel ward) {
        if (!subCounty.getCounty().getId().equals(county.getId())) {
            throw new ValidationException("Sub-county does not belong to the selected county");
        }
        if (!ward.getSubCounty().getId().equals(subCounty.getId())) {
            throw new ValidationException("Ward does not belong to the selected sub-county");
        }
    }

    public SystemUserModel currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ValidationException("Authenticated user is required");
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private void ensureSubCountyAssigned(SystemUserModel user) {
        if (user.getAssignedSubCounty() == null) {
            throw new ValidationException("Your account has no assigned sub-county");
        }
    }

    private void ensureWardsAssigned(SystemUserModel user) {
        if (user.getAssignedWards() == null || user.getAssignedWards().isEmpty()) {
            throw new ValidationException("Your account has no assigned wards");
        }
    }
}

package com.SUNData.MemberApp.Service.member;

import com.SUNData.MemberApp.DTOs.Member.*;
import com.SUNData.MemberApp.Enums.RegistrationType;
import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Exceptions.ResourceNotFoundException;
import com.SUNData.MemberApp.Exceptions.ValidationException;
import com.SUNData.MemberApp.Model.GroupModel.MemberGroupModel;
import com.SUNData.MemberApp.Model.LocationModel.CountyModel;
import com.SUNData.MemberApp.Model.LocationModel.SubCountyModel;
import com.SUNData.MemberApp.Model.LocationModel.WardModel;
import com.SUNData.MemberApp.Model.MemberModel.DependantModel;
import com.SUNData.MemberApp.Model.MemberModel.NextOfKinModel;
import com.SUNData.MemberApp.Model.MemberModel.PrincipalMemberModel;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.CountyRepository;
import com.SUNData.MemberApp.Repository.DependantRepository;
import com.SUNData.MemberApp.Repository.PrincipalMemberRepository;
import com.SUNData.MemberApp.Repository.SubCountyRepository;
import com.SUNData.MemberApp.Repository.SystemUserRepository;
import com.SUNData.MemberApp.Repository.WardRepository;
import com.SUNData.MemberApp.Service.group.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    private final PrincipalMemberRepository principalRepo;
    private final DependantRepository dependantRepo;
    private final SystemUserRepository userRepo;
    private final CountyRepository countyRepo;
    private final SubCountyRepository subCountyRepo;
    private final WardRepository wardRepo;
    private final GroupService groupService;

    public MemberService(
            PrincipalMemberRepository principalRepo,
            DependantRepository dependantRepo,
            SystemUserRepository userRepo,
            CountyRepository countyRepo,
            SubCountyRepository subCountyRepo,
            WardRepository wardRepo,
            GroupService groupService) {
        this.principalRepo = principalRepo;
        this.dependantRepo = dependantRepo;
        this.userRepo = userRepo;
        this.countyRepo = countyRepo;
        this.subCountyRepo = subCountyRepo;
        this.wardRepo = wardRepo;
        this.groupService = groupService;
    }

    @Transactional
    public MemberDetailsDTO registerMember(RegisterMemberRequestDTO request) {
        PrincipalMemberDTO dto = request.getPrincipal();
        log.info("Attempting to register Principal Member with NationalID={} and Phone={}",
                dto.getNationalID(), dto.getPhoneNumber());

        validateUniqueNationalId(dto.getNationalID(), null);
        validateUniquePhoneNumber(dto.getPhoneNumber(), null);

        if (request.getNextOfKin() == null) {
            throw new ValidationException("Next Of Kin is mandatory");
        }

        PrincipalMemberModel principal = dto.toEntity();
        stampRegistrar(principal);
        applyRegistrationScope(principal, dto);

        NextOfKinModel kin = request.getNextOfKin().toEntity();
        principal.setNextOfKin(kin);

        PrincipalMemberModel savedPrincipal = principalRepo.save(principal);

        if (request.getDependants() != null) {
            if (request.getDependants().size() > 6) {
                throw new ValidationException("Maximum 6 dependants allowed");
            }
            for (DependantDTO depDto : request.getDependants()) {
                DependantModel dependant = depDto.toEntity();
                dependant.setPrincipalMember(savedPrincipal);
                dependantRepo.save(dependant);
            }
        }

        return getMemberById(savedPrincipal.getId());
    }

    @Transactional(readOnly = true)
    public List<MemberDetailsDTO> getAllMembers() {
        SystemUserModel user = groupService.currentUser();
        log.info("Fetching principal members for user={} role={}", user.getEmail(), user.getRole());
        return getVisiblePrincipals(user).stream()
                .map(this::toMemberDetails)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberExistsResponseDTO memberExists(String nationalId, String phoneNumber) {
        String normalizedNationalId = normalizeQueryValue(nationalId);
        String normalizedPhoneNumber = normalizeQueryValue(phoneNumber);

        if (normalizedNationalId == null && normalizedPhoneNumber == null) {
            throw new ValidationException("At least one of nationalId or phoneNumber must be provided");
        }

        boolean nationalIdExists = normalizedNationalId != null
                && principalRepo.existsByNationalID(normalizedNationalId);
        boolean phoneNumberExists = normalizedPhoneNumber != null
                && principalRepo.existsByPhoneNumber(normalizedPhoneNumber);

        return new MemberExistsResponseDTO(nationalIdExists, phoneNumberExists);
    }

    @Transactional(readOnly = true)
    public MemberDetailsDTO getMemberById(Long id) {
        PrincipalMemberModel principal = getPrincipalOrThrow(id);
        validateCurrentUserCanViewPrincipal(principal);
        return toMemberDetails(principal);
    }

    @Transactional(readOnly = true)
    public MemberDetailsDTO getMemberByNationalId(String nationalId) {
        PrincipalMemberModel principal = getPrincipalByNationalIdOrThrow(nationalId);
        validateCurrentUserCanViewPrincipal(principal);
        return toMemberDetails(principal);
    }

    @Transactional
    public PrincipalMemberDTO updatePrincipal(Long id, PrincipalMemberDTO dto) {
        PrincipalMemberModel existing = getPrincipalOrThrow(id);

        validateUniqueNationalId(dto.getNationalID(), existing.getId());
        validateUniquePhoneNumber(dto.getPhoneNumber(), existing.getId());

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setNationalID(dto.getNationalID());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setGroupName(dto.getGroupName());
        applyRegistrationScope(existing, dto);

        log.info("Updated Principal Member ID={}", id);
        return new PrincipalMemberDTO(principalRepo.save(existing));
    }

    @Transactional
    public PrincipalMemberDTO patchPrincipal(Long id, PrincipalMemberDTO dto) {
        log.info("Patching Principal Member ID={}", id);
        PrincipalMemberModel existing = getPrincipalOrThrow(id);

        if (dto.getNationalID() != null) {
            validateUniqueNationalId(dto.getNationalID(), existing.getId());
        }
        if (dto.getPhoneNumber() != null) {
            validateUniquePhoneNumber(dto.getPhoneNumber(), existing.getId());
        }

        applyPrincipalPatch(existing, dto);
        if (hasLocationOrGroupPatch(dto)) {
            applyRegistrationScope(existing, dto);
        }

        PrincipalMemberModel updated = principalRepo.save(existing);
        log.info("Successfully patched Principal Member ID={}", id);
        return new PrincipalMemberDTO(updated);
    }

    @Transactional
    public MemberDetailsDTO transferMember(Long id, TransferMemberRequestDTO request) {
        PrincipalMemberModel principal = getPrincipalOrThrow(id);
        WardModel ward = getWardOrThrow(request.getWardId());
        SubCountyModel subCounty = ward.getSubCounty();
        CountyModel county = subCounty.getCounty();

        groupService.validateUserCanAccessWard(groupService.currentUser(), ward);

        RegistrationType registrationType = request.getRegistrationType() != null
                ? request.getRegistrationType()
                : RegistrationType.INDIVIDUAL;

        principal.setCounty(county);
        principal.setSubCounty(subCounty);
        principal.setWard(ward);
        principal.setRegistrationType(registrationType);

        if (registrationType == RegistrationType.GROUP) {
            if (request.getGroupId() == null) {
                throw new ValidationException("Group is required for group transfer", Map.of(
                        "groupId", "Group is required for group transfer"
                ));
            }

            MemberGroupModel group = groupService.getAccessibleGroupOrThrow(
                    request.getGroupId(),
                    ward.getId()
            );
            principal.setGroup(group);
            principal.setGroupName(group.getName());
        } else {
            principal.setGroup(null);
            principal.setGroupName(null);
        }

        PrincipalMemberModel updated = principalRepo.save(principal);
        log.info("Transferred Principal Member ID={} to Ward ID={} and Group ID={}",
                id, ward.getId(), request.getGroupId());
        return toMemberDetails(updated);
    }

    @Transactional
    public NextOfKinDTO updateNextOfKin(Long principalId, NextOfKinDTO dto) {
        PrincipalMemberModel principal = getPrincipalOrThrow(principalId);
        NextOfKinModel updatedKin = dto.toEntity();
        principal.setNextOfKin(updatedKin);
        PrincipalMemberModel saved = principalRepo.save(principal);
        log.info("Updated Next of Kin for Principal Member ID={}", principalId);
        return new NextOfKinDTO(saved.getNextOfKin());
    }

    @Transactional
    public NextOfKinDTO patchNextOfKin(Long principalId, NextOfKinDTO dto) {
        PrincipalMemberModel principal = getPrincipalOrThrow(principalId);
        NextOfKinModel kin = principal.getNextOfKin();
        if (kin == null) {
            throw new ResourceNotFoundException("Next of Kin not found for Principal Member ID=" + principalId);
        }

        applyNextOfKinPatch(kin, dto);
        principal.setNextOfKin(kin);
        principalRepo.save(principal);
        log.info("Patched Next of Kin for Principal Member ID={}", principalId);
        return new NextOfKinDTO(kin);
    }

    @Transactional
    public void deleteNextOfKin(Long principalId) {
        PrincipalMemberModel principal = getPrincipalOrThrow(principalId);

        if (principal.getNextOfKin() == null) {
            throw new ResourceNotFoundException("Next of Kin not found for Principal Member ID=" + principalId);
        }

        principal.setNextOfKin(null);
        principalRepo.save(principal);
        log.info("Deleted Next of Kin for Principal Member ID={}", principalId);
    }

    @Transactional
    public DependantDTO addDependant(Long principalId, DependantDTO dto) {
        PrincipalMemberModel principal = getPrincipalOrThrow(principalId);
        validateDependantLimit(principalId);

        DependantModel dependant = dto.toEntity();
        dependant.setPrincipalMember(principal);
        DependantModel savedDependant = dependantRepo.save(dependant);

        log.info("Added Dependant ID={} to Principal Member ID={}", savedDependant.getId(), principalId);
        return new DependantDTO(savedDependant);
    }

    @Transactional
    public DependantDTO patchDependant(Long dependantId, DependantDTO dto) {
        DependantModel dependant = getDependantOrThrow(dependantId);
        applyDependantPatch(dependant, dto);
        DependantModel updated = dependantRepo.save(dependant);
        log.info("Patched Dependant ID={}", dependantId);
        return new DependantDTO(updated);
    }

    @Transactional
    public void deleteDependant(Long principalId, Long dependantId) {
        PrincipalMemberModel principal = getPrincipalOrThrow(principalId);
        DependantModel dependant = getDependantOrThrow(dependantId);

        if (!dependant.getPrincipalMember().getId().equals(principal.getId())) {
            log.warn("Dependant ID={} does not belong to Principal Member ID={}", dependantId, principalId);
            throw new ValidationException("Dependant does not belong to this Principal Member");
        }

        principal.getDependants().remove(dependant);
        principalRepo.save(principal);
        log.info("Deleted Dependant ID={} from Principal Member ID={}", dependantId, principalId);
    }

    private MemberDetailsDTO toMemberDetails(PrincipalMemberModel principal) {
        NextOfKinDTO kinDTO = principal.getNextOfKin() != null
                ? new NextOfKinDTO(principal.getNextOfKin())
                : null;
        List<DependantDTO> dependantDTOs = dependantRepo.findByPrincipalMemberId(principal.getId())
                .stream()
                .map(DependantDTO::new)
                .toList();
        return new MemberDetailsDTO(new PrincipalMemberDTO(principal), kinDTO, dependantDTOs);
    }

    public List<PrincipalMemberDTO> getAccessibleMembersForGroup(Long groupId) {
        return principalRepo.findByGroupId(groupId).stream()
                .peek(this::validateCurrentUserCanViewPrincipal)
                .map(PrincipalMemberDTO::new)
                .toList();
    }

    private List<PrincipalMemberModel> getVisiblePrincipals(SystemUserModel user) {
        if (user.getRole() == UserRole.ADMIN) {
            return principalRepo.findAll();
        }

        if (user.getRole() == UserRole.COORDINATOR) {
            if (user.getAssignedCounty() != null) {
                return principalRepo.findByCountyId(user.getAssignedCounty().getId());
            }
            if (user.getAssignedSubCounty() != null) {
                return principalRepo.findBySubCountyId(user.getAssignedSubCounty().getId());
            }
            return List.of();
        }

        if (user.getAssignedWards() == null || user.getAssignedWards().isEmpty()) {
            return List.of();
        }

        Set<Long> wardIds = user.getAssignedWards().stream()
                .map(WardModel::getId)
                .collect(java.util.stream.Collectors.toSet());
        return principalRepo.findByWardIdIn(wardIds);
    }

    private void validateCurrentUserCanViewPrincipal(PrincipalMemberModel principal) {
        if (principal.getWard() == null) {
            if (groupService.currentUser().getRole() == UserRole.ADMIN) {
                return;
            }
            throw new ValidationException("Member is not assigned to your working area");
        }
        groupService.validateUserCanAccessWard(groupService.currentUser(), principal.getWard());
    }

    private PrincipalMemberModel getPrincipalOrThrow(Long id) {
        return principalRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Principal Member not found: " + id));
    }

    private PrincipalMemberModel getPrincipalByNationalIdOrThrow(String nationalId) {
        return principalRepo.findByNationalID(nationalId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Principal Member not found with National ID: " + nationalId));
    }

    private DependantModel getDependantOrThrow(Long id) {
        return dependantRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dependant not found"));
    }

    private void validateDependantLimit(Long principalId) {
        long count = dependantRepo.countByPrincipalMemberId(principalId);
        if (count >= 6) {
            log.warn("Dependant limit exceeded for Principal Member ID={}", principalId);
            throw new ValidationException("Maximum 6 dependants allowed");
        }
    }

    private void applyRegistrationScope(PrincipalMemberModel principal, PrincipalMemberDTO dto) {
        RegistrationType registrationType = dto.getRegistrationType() != null
                ? dto.getRegistrationType()
                : principal.getRegistrationType();
        if (registrationType == null) {
            throw new ValidationException("Registration type is required", Map.of(
                    "principal.registrationType", "Registration type is required"
            ));
        }

        Long countyId = dto.getCountyId() != null
                ? dto.getCountyId()
                : principal.getCounty() != null ? principal.getCounty().getId() : null;
        Long subCountyId = dto.getSubCountyId() != null
                ? dto.getSubCountyId()
                : principal.getSubCounty() != null ? principal.getSubCounty().getId() : null;
        Long wardId = dto.getWardId() != null
                ? dto.getWardId()
                : principal.getWard() != null ? principal.getWard().getId() : null;

        CountyModel county = getCountyOrThrow(countyId);
        SubCountyModel subCounty = getSubCountyOrThrow(subCountyId);
        WardModel ward = getWardOrThrow(wardId);

        groupService.validateLocationHierarchy(county, subCounty, ward);
        groupService.validateUserCanAccessWard(groupService.currentUser(), ward);

        principal.setRegistrationType(registrationType);
        principal.setCounty(county);
        principal.setSubCounty(subCounty);
        principal.setWard(ward);

        if (registrationType == RegistrationType.GROUP) {
            Long groupId = dto.getGroupId() != null
                    ? dto.getGroupId()
                    : principal.getGroup() != null ? principal.getGroup().getId() : null;
            if (groupId == null) {
                throw new ValidationException("Group is required for group member registration", Map.of(
                        "principal.groupId", "Group is required for group member registration"
                ));
            }

            MemberGroupModel group = groupService.getAccessibleGroupOrThrow(groupId, ward.getId());
            principal.setGroup(group);
            principal.setGroupName(group.getName());
            return;
        }

        principal.setGroup(null);
        principal.setGroupName(null);
    }

    private boolean hasLocationOrGroupPatch(PrincipalMemberDTO dto) {
        return dto.getRegistrationType() != null
                || dto.getCountyId() != null
                || dto.getSubCountyId() != null
                || dto.getWardId() != null
                || dto.getGroupId() != null;
    }

    private CountyModel getCountyOrThrow(Long countyId) {
        if (countyId == null) {
            throw new ValidationException("County is required", Map.of(
                    "principal.countyId", "County is required"
            ));
        }
        return countyRepo.findById(countyId)
                .orElseThrow(() -> new ValidationException("County not found", Map.of(
                        "principal.countyId", "County not found"
                )));
    }

    private SubCountyModel getSubCountyOrThrow(Long subCountyId) {
        if (subCountyId == null) {
            throw new ValidationException("Sub-county is required", Map.of(
                    "principal.subCountyId", "Sub-county is required"
            ));
        }
        return subCountyRepo.findById(subCountyId)
                .orElseThrow(() -> new ValidationException("Sub-county not found", Map.of(
                        "principal.subCountyId", "Sub-county not found"
                )));
    }

    private WardModel getWardOrThrow(Long wardId) {
        if (wardId == null) {
            throw new ValidationException("Ward is required", Map.of(
                    "principal.wardId", "Ward is required"
            ));
        }
        return wardRepo.findById(wardId)
                .orElseThrow(() -> new ValidationException("Ward not found", Map.of(
                        "principal.wardId", "Ward not found"
                )));
    }

    private void validateUniqueNationalId(String nationalId, Long currentMemberId) {
        if (nationalId == null) {
            return;
        }

        principalRepo.findByNationalID(nationalId)
                .filter(existing -> !existing.getId().equals(currentMemberId))
                .ifPresent(existing -> {
                    throw new ValidationException("National ID already exists", Map.of(
                            "principal.nationalID", "National ID already exists"
                    ));
                });
    }

    private void validateUniquePhoneNumber(String phoneNumber, Long currentMemberId) {
        if (phoneNumber == null) {
            return;
        }

        principalRepo.findByPhoneNumber(phoneNumber)
                .filter(existing -> !existing.getId().equals(currentMemberId))
                .ifPresent(existing -> {
                    throw new ValidationException("Phone number already exists", Map.of(
                            "principal.phoneNumber", "Phone number already exists"
                    ));
                });
    }

    private String normalizeQueryValue(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void applyPrincipalPatch(PrincipalMemberModel existing, PrincipalMemberDTO dto) {
        if (dto.getFirstName() != null) existing.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) existing.setLastName(dto.getLastName());
        if (dto.getNationalID() != null) existing.setNationalID(dto.getNationalID());
        if (dto.getPhoneNumber() != null) existing.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getDateOfBirth() != null) existing.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getGroupName() != null) existing.setGroupName(dto.getGroupName());
    }

    private void applyNextOfKinPatch(NextOfKinModel kin, NextOfKinDTO dto) {
        if (dto.getFirstName() != null) kin.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) kin.setLastName(dto.getLastName());
        if (dto.getRelationship() != null) kin.setRelationship(dto.getRelationship());
        if (dto.getGender() != null) kin.setGender(dto.getGender());
        if (dto.getIdNumber() != null) kin.setIdNumber(dto.getIdNumber());
        if (dto.getPhoneNumber() != null) kin.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getDateOfBirth() != null) kin.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getIdAttachmentPath() != null) kin.setIdAttachmentPath(dto.getIdAttachmentPath());
    }

    private void applyDependantPatch(DependantModel dependant, DependantDTO dto) {
        if (dto.getFirstName() != null) dependant.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) dependant.setLastName(dto.getLastName());
        if (dto.getDateOfBirth() != null) dependant.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getRelationship() != null) dependant.setRelationship(dto.getRelationship());
        if (dto.getGender() != null) dependant.setGender(dto.getGender());
        if (dto.getPhoneNumber() != null) dependant.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getBirthCertificatePath() != null) dependant.setBirthCertificatePath(dto.getBirthCertificatePath());
    }

    private void stampRegistrar(PrincipalMemberModel principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }

        String email = authentication.getName();
        String displayName = userRepo.findByEmail(email)
                .map(SystemUserModel::getFullName)
                .orElse(email);
        principal.setRegisteredByName(displayName);

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring(5))
                .findFirst()
                .orElse("USER");
        principal.setRegisteredByRole(role);
    }
}

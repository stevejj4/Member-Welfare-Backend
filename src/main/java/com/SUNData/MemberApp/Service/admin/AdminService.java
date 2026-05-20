package com.SUNData.MemberApp.Service.admin;

import com.SUNData.MemberApp.DTOs.Member.*;
import com.SUNData.MemberApp.DTOs.User.*;
import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Exceptions.ResourceNotFoundException;
import com.SUNData.MemberApp.Exceptions.ValidationException;
import com.SUNData.MemberApp.Model.MemberModel.*;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class AdminService {

    private final PrincipalMemberRepository principalRepo;
    private final NextOfKinRepository nextOfKinRepo;
    private final DependantRepository dependantRepo;
    private final SystemUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    public AdminService(PrincipalMemberRepository principalRepo,
                        NextOfKinRepository nextOfKinRepo,
                        DependantRepository dependantRepo,
                        SystemUserRepository userRepo,
                        PasswordEncoder passwordEncoder) {
        this.principalRepo = principalRepo;
        this.nextOfKinRepo = nextOfKinRepo;
        this.dependantRepo = dependantRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------------- USER MANAGEMENT ----------------

    /** Create new system user (Facilitator/Coordinator) */
    public UserDTO createUser(RegisterUserDTO dto) {
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new ValidationException("Email already exists");
        }
        SystemUserModel user = new SystemUserModel();
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setRole(dto.getRole());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // ready password
        log.info("Created new user with email={}", dto.getEmail());
        return new UserDTO(userRepo.save(user));
    }

    /** Reset any user's password */
    public void resetPassword(Long userId, String newPassword) {
        SystemUserModel user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        log.info("Password reset for user ID={}", userId);
    }

    /** View all system users */
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream().map(UserDTO::new).toList();
    }

    /** Assign/revoke/update roles */
    public UserDTO updateUserRole(Long userId, UserRole newRole) {
        SystemUserModel user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(newRole);
        log.info("Updated role for user ID={} to {}", userId, newRole);
        return new UserDTO(userRepo.save(user));
    }

    // ---------------- MEMBER MANAGEMENT ----------------
    // (Your existing member logic: register, update, patch, add dependants, etc.)
    // I’ve kept all your methods intact: getAllMembers, registerFullMember, updatePrincipal,
    // patchPrincipal, updateNextOfKin, patchNextOfKin, addDependant, patchDependant,
    // deletePrincipal, deleteDependant, getFullMemberDetails, getFullMemberDetailsByNationalId.


// ----------------- Helper Methods -----------------

    /** Fetch Principal Member or throw if not found */
    private PrincipalMemberModel getPrincipalOrThrow(Long id) {
        return principalRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Principal Member not found: " +id));
    }
    /** Fetch by BUSINESS KEY (National ID) */
    private PrincipalMemberModel getPrincipalByNationalIdOrThrow(String nationalId) {
        return principalRepo.findByNationalID(nationalId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Principal Member not found with National ID: " + nationalId));
    }

    /** Fetch Dependant or throw if not found */
    private DependantModel getDependantOrThrow(Long id) {
        return dependantRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dependant not found"));
    }

    /** Validate that a Principal Member does not exceed 6 dependants */
    private void validateDependantLimit(Long principalId) {
        List<DependantModel> existingDependants = dependantRepo.findByPrincipalMemberId(principalId);
        if (existingDependants.size() >= 6) {
            log.warn("Dependant limit exceeded for Principal Member ID={}", principalId);
            throw new IllegalStateException("Maximum 6 dependants allowed");
        }
    }

    /** Apply partial updates to Principal Member fields */
    private void applyPrincipalPatch(PrincipalMemberModel existing, PrincipalMemberDTO dto) {
        if (dto.getFirstName() != null) existing.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) existing.setLastName(dto.getLastName());
        if (dto.getNationalID() != null) existing.setNationalID(dto.getNationalID());
        if (dto.getPhoneNumber() != null) existing.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getDateOfBirth() != null) existing.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getGroupName() != null) existing.setGroupName(dto.getGroupName());
    }

    /** Apply partial updates to Next of Kin fields */
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

    /** Apply partial updates to Dependant fields */
    private void applyDependantPatch(DependantModel dependant, DependantDTO dto) {
        if (dto.getFirstName() != null) dependant.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) dependant.setLastName(dto.getLastName());
        if (dto.getDateOfBirth() != null) dependant.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getRelationship() != null) dependant.setRelationship(dto.getRelationship());
        if (dto.getGender() != null) dependant.setGender(dto.getGender());
        if (dto.getPhoneNumber() != null) dependant.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getBirthCertificatePath() != null) dependant.setBirthCertificatePath(dto.getBirthCertificatePath());
    }

    // ----------------- Business Methods -----------------
    /**
     * Get all the members in the db
     * for production purposes I would used pagination
     */
    @Transactional
    public List<MemberDetailsDTO> getAllMembers() {
        log.info("Fetching all principal members");
        return principalRepo.findAll().stream().map(principal -> {
            PrincipalMemberDTO memberDTO = new PrincipalMemberDTO(principal);
            NextOfKinDTO kinDTO = principal.getNextOfKin() != null ? new NextOfKinDTO(principal.getNextOfKin()) : null;
            List<DependantDTO> dependantDTOs = dependantRepo.findByPrincipalMemberId(principal.getId())
                    .stream().map(DependantDTO::new).toList();
            return new MemberDetailsDTO(memberDTO, kinDTO, dependantDTOs);
        }).toList();
    }
    /**
     * Register a full Principal Member aggregate:
     * - Principal Member (mandatory)
     * - Next of Kin (mandatory)
     * - Dependants (optional, max 6)
     */
    // for internal use
    @Transactional
    public MemberDetailsDTO getFullMemberDetails(Long id) {
        log.debug("Fetching full details for Principal Member ID={}", id);
        PrincipalMemberModel principal = getPrincipalOrThrow(id);
        NextOfKinDTO kinDTO = principal.getNextOfKin() != null ? new NextOfKinDTO(principal.getNextOfKin()) : null;
        List<DependantDTO> dependantDTOs = dependantRepo.findByPrincipalMemberId(id)
                .stream().map(DependantDTO::new).toList();
        return new MemberDetailsDTO(new PrincipalMemberDTO(principal), kinDTO, dependantDTOs);
    }
    // Role Facilitator, Coordinator and Admin
    @Transactional
    public MemberDetailsDTO getFullMemberDetailsByNationalId(String nationalId) {
        log.info("Fetching member by National ID={}", nationalId);
        PrincipalMemberModel principal = getPrincipalByNationalIdOrThrow(nationalId);
        NextOfKinDTO kinDTO = principal.getNextOfKin() != null ? new NextOfKinDTO(principal.getNextOfKin()) : null;
        List<DependantDTO> dependantDTOs = dependantRepo.findByPrincipalMemberId(principal.getId())
                .stream().map(DependantDTO::new).toList();
        return new MemberDetailsDTO(new PrincipalMemberDTO(principal), kinDTO, dependantDTOs);
    }

    // database operations are commited as a single unit of work
    @Transactional
    public MemberDetailsDTO registerFullMember(RegisterMemberRequestDTO request) {
        PrincipalMemberDTO dto = request.getPrincipal();
        log.info("Attempting to register Principal Member with NationalID={} and Phone={}",
                dto.getNationalID(), dto.getPhoneNumber());

        // Check for duplicates
        if (principalRepo.existsByNationalID(dto.getNationalID())) {
            log.warn("Duplicate NationalID detected: {}", dto.getNationalID());
            throw new ValidationException("National ID already exists");
        }
        if (principalRepo.existsByPhoneNumber(dto.getPhoneNumber())) {
            log.warn("Duplicate PhoneNumber detected: {}", dto.getPhoneNumber());
            throw new ValidationException("Phone number already exists");
        }

        PrincipalMemberModel principal = dto.toEntity();

        if (request.getNextOfKin() == null) {
            throw new ValidationException("Next Of Kin is mandatory");
        }
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

        return getFullMemberDetails(savedPrincipal.getId());
    }

    /** Full update of Principal Member (PUT-style) */
    @Transactional
    public PrincipalMemberDTO updatePrincipal(Long id, PrincipalMemberDTO dto) {
        PrincipalMemberModel existing = getPrincipalOrThrow(id);
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setNationalID(dto.getNationalID());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setGroupName(dto.getGroupName());
        log.info("Updated Principal Member ID={}", id);
        return new PrincipalMemberDTO(principalRepo.save(existing));
    }

    /** Partial update of Principal Member (PATCH-style) */
    @Transactional
    public PrincipalMemberDTO patchPrincipal(Long id, PrincipalMemberDTO dto) {
        log.info("Patching Principal Member ID={}", id);
        PrincipalMemberModel existing = getPrincipalOrThrow(id);
        applyPrincipalPatch(existing, dto);

        if (dto.getNationalID() != null && principalRepo.existsByNationalID(dto.getNationalID())
                && !dto.getNationalID().equals(existing.getNationalID())) {
            throw new ValidationException("National ID already exists");
        }

        if (dto.getPhoneNumber() != null && principalRepo.existsByPhoneNumber(dto.getPhoneNumber())
                && !dto.getPhoneNumber().equals(existing.getPhoneNumber())) {
            throw new ValidationException("Phone number already exists");
        }

        PrincipalMemberModel updated = principalRepo.save(existing);
        log.info("Successfully patched Principal Member ID={}", id);
        return new PrincipalMemberDTO(updated);
    }

    /** Full update of Next of Kin (PUT-style) */
    @Transactional
    public NextOfKinDTO updateNextOfKin(Long principalId, NextOfKinDTO dto) {
        PrincipalMemberModel principal = getPrincipalOrThrow(principalId);
        NextOfKinModel updatedKin = dto.toEntity();
        principal.setNextOfKin(updatedKin);
        PrincipalMemberModel saved = principalRepo.save(principal);
        log.info("Updated Next of Kin for Principal Member ID={}", principalId);
        return new NextOfKinDTO(saved.getNextOfKin());
    }

    /** Partial update of Next of Kin (PATCH-style) */
    @Transactional
    public NextOfKinDTO patchNextOfKin(Long principalId, NextOfKinDTO dto) {
        PrincipalMemberModel principal = getPrincipalOrThrow(principalId);
        NextOfKinModel kin = principal.getNextOfKin();
        applyNextOfKinPatch(kin, dto);
        principal.setNextOfKin(kin);
        principalRepo.save(principal);
        log.info("Patched Next of Kin for Principal Member ID={}", principalId);
        return new NextOfKinDTO(kin);
    }

    /** Add a new dependant to a Principal Member (max 6 allowed) */
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

    /** Partial update of a Dependant (PATCH-style) */
    @Transactional
    public DependantDTO patchDependant(Long dependantId, DependantDTO dto) {
        DependantModel dependant = getDependantOrThrow(dependantId);
        applyDependantPatch(dependant, dto);
        DependantModel updated = dependantRepo.save(dependant);
        log.info("Patched Dependant ID={}", dependantId);
        return new DependantDTO(updated);
    }

    /** Delete a Principal Member (cascade deletes Next of Kin and Dependants) */
    @Transactional
    public void deletePrincipal(Long id) {
        if (!principalRepo.existsById(id)) {
            throw new ResourceNotFoundException("Member not found with ID=" + id);
        }
        principalRepo.deleteById(id);
        log.info("Deleted Principal Member ID={}", id);
    }

    /** Delete a dependant through its Principal Member */
    @Transactional
    public void deleteDependant(Long principalId, Long dependantId) {
        PrincipalMemberModel principal = getPrincipalOrThrow(principalId);
        DependantModel dependant = getDependantOrThrow(dependantId);

        if (!dependant.getPrincipalMember().getId().equals(principal.getId())) {
            log.warn("Dependant ID={} does not belong to Principal Member ID={}", dependantId, principalId);
            throw new ValidationException("Dependant does not belong to this Principal Member");
        }

        principal.getDependants().remove(dependant);
        principalRepo.save(principal); // orphanRemoval triggers delete
        log.info("Deleted Dependant ID={} from Principal Member ID={}", dependantId, principalId);
    }
}

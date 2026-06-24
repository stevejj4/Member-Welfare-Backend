package com.SUNData.MemberApp.Service.admin;

import com.SUNData.MemberApp.DTOs.User.CreateUserRequestDTO;
import com.SUNData.MemberApp.DTOs.User.CreateUserResponseDTO;
import com.SUNData.MemberApp.DTOs.User.UpdateUserRequestDTO;
import com.SUNData.MemberApp.DTOs.User.UserDTO;
import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Exceptions.ResourceNotFoundException;
import com.SUNData.MemberApp.Exceptions.ValidationException;
import com.SUNData.MemberApp.Model.LocationModel.CountyModel;
import com.SUNData.MemberApp.Model.LocationModel.SubCountyModel;
import com.SUNData.MemberApp.Model.LocationModel.WardModel;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.CountyRepository;
import com.SUNData.MemberApp.Repository.SubCountyRepository;
import com.SUNData.MemberApp.Repository.SystemUserRepository;
import com.SUNData.MemberApp.Repository.WardRepository;
import com.SUNData.MemberApp.Service.mail.EmailService;
import com.SUNData.MemberApp.Util.SecureTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminUserService {

    private static final Logger log = LoggerFactory.getLogger(AdminUserService.class);

    private final SystemUserRepository userRepository;
    private final CountyRepository countyRepository;
    private final SubCountyRepository subCountyRepository;
    private final WardRepository wardRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AdminUserService(
            SystemUserRepository userRepository,
            CountyRepository countyRepository,
            SubCountyRepository subCountyRepository,
            WardRepository wardRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.countyRepository = countyRepository;
        this.subCountyRepository = subCountyRepository;
        this.wardRepository = wardRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserDTO::new).toList();
    }

    @Transactional
    public CreateUserResponseDTO provisionUser(CreateUserRequestDTO request) {
        validateAssignableRole(request.getAssignedRole());

        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new ValidationException("Email already exists");
        }

        String fullName = buildFullName(request.getFirstName(), request.getLastName());
        String temporaryPassword = SecureTokenGenerator.generateTemporaryPassword();

        SystemUserModel user = new SystemUserModel();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole(request.getAssignedRole());
        applyAreaAssignment(user, request);
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        user.setMustChangePassword(true);
        user.setResetToken(null);

        SystemUserModel saved = userRepository.save(user);
        log.info("Provisioned user id={} email={} role={}", saved.getId(), email, saved.getRole());

        emailService.sendWelcomeCredentials(
                email,
                fullName,
                formatRoleLabel(saved.getRole()),
                temporaryPassword
        );

        return new CreateUserResponseDTO(
                new UserDTO(saved),
                "User created successfully. Login credentials have been sent to their email."
        );
    }

    @Transactional
    public void adminResetPassword(Long userId, String newPassword) {
        SystemUserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(true);
        userRepository.save(user);
        log.info("Admin reset password for user id={}", userId);
    }

    @Transactional
    public UserDTO updateUser(UpdateUserRequestDTO request, Long userId) {
        SystemUserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String email = request.getEmail().trim().toLowerCase();
        if (!user.getEmail().equalsIgnoreCase(email) && userRepository.existsByEmail(email)) {
            throw new ValidationException("Email already exists");
        }

        user.setEmail(email);
        user.setFullName(buildFullName(request.getFirstName(), request.getLastName()));
        if (request.getAssignedRole() != null) {
            validateAssignableRole(request.getAssignedRole());
            user.setRole(request.getAssignedRole());
        }
        if (hasAreaAssignmentPatch(request)) {
            applyAreaAssignment(user, user.getRole(), request.getCountyId(), request.getSubCountyId(), request.getWardIds());
        }
        return new UserDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO updateUserRole(Long userId, UserRole newRole) {
        validateAssignableRole(newRole);
        SystemUserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(newRole);
        user.getAssignedWards().clear();
        return new UserDTO(userRepository.save(user));
    }

    private void validateAssignableRole(UserRole role) {
        if (role != UserRole.FACILITATOR && role != UserRole.COORDINATOR) {
            throw new ValidationException("Only Facilitator and Coordinator roles can be provisioned by admins");
        }
    }

    private boolean hasAreaAssignmentPatch(UpdateUserRequestDTO request) {
        return request.getAssignedRole() != null
                || request.getCountyId() != null
                || request.getSubCountyId() != null
                || request.getWardIds() != null;
    }

    private void applyAreaAssignment(SystemUserModel user, CreateUserRequestDTO request) {
        applyAreaAssignment(
                user,
                request.getAssignedRole(),
                request.getCountyId(),
                request.getSubCountyId(),
                request.getWardIds()
        );
    }

    private void applyAreaAssignment(
            SystemUserModel user,
            UserRole assignedRole,
            Long countyId,
            Long subCountyId,
            List<Long> wardIds) {
        CountyModel county = getCountyOrThrow(countyId);
        SubCountyModel subCounty = getSubCountyOrThrow(subCountyId);
        validateSubCountyBelongsToCounty(subCounty, county.getId());

        user.setAssignedCounty(county);
        user.setAssignedSubCounty(subCounty);
        user.getAssignedWards().clear();

        if (assignedRole == UserRole.FACILITATOR) {
            if (wardIds == null || wardIds.isEmpty()) {
                throw new ValidationException("Facilitator must be assigned at least one ward");
            }

            Set<WardModel> wards = new HashSet<>(wardRepository.findByIdIn(wardIds));
            if (wards.size() != new HashSet<>(wardIds).size()) {
                throw new ValidationException("One or more assigned wards were not found");
            }
            wards.forEach(ward -> validateWardBelongsToSubCounty(ward, subCounty.getId()));
            user.setAssignedWards(wards);
        }
    }

    private CountyModel getCountyOrThrow(Long countyId) {
        if (countyId == null) {
            throw new ValidationException("County assignment is required");
        }
        return countyRepository.findById(countyId)
                .orElseThrow(() -> new ValidationException("County not found"));
    }

    private SubCountyModel getSubCountyOrThrow(Long subCountyId) {
        if (subCountyId == null) {
            throw new ValidationException("Sub-county assignment is required");
        }
        return subCountyRepository.findById(subCountyId)
                .orElseThrow(() -> new ValidationException("Sub-county not found"));
    }

    private void validateSubCountyBelongsToCounty(SubCountyModel subCounty, Long countyId) {
        if (!subCounty.getCounty().getId().equals(countyId)) {
            throw new ValidationException("Sub-county does not belong to the selected county");
        }
    }

    private void validateWardBelongsToSubCounty(WardModel ward, Long subCountyId) {
        if (!ward.getSubCounty().getId().equals(subCountyId)) {
            throw new ValidationException("Ward does not belong to the selected sub-county");
        }
    }

    private String buildFullName(String firstName, String lastName) {
        return (firstName.trim() + " " + lastName.trim()).trim();
    }

    private String formatRoleLabel(UserRole role) {
        if (role == null) {
            return "User";
        }
        String lower = role.name().toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}

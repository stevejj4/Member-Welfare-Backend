package com.SUNData.MemberApp.Service;

import com.SUNData.MemberApp.DTOs.*;
import com.SUNData.MemberApp.Model.*;
import com.SUNData.MemberApp.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for managing Principal Members and their aggregate:
 * - Next of Kin (mandatory, lifecycle tied to Principal Member)
 * - Dependants (optional, max 6, lifecycle tied to Principal Member)
 *
 * This class enforces business rules and ensures all operations
 * on NextOfKin and Dependants are scoped through the Principal Member.
 */
@Service
public class PrincipalMemberService {

    private final PrincipalMemberRepository principalRepo;
    private final NextOfKinRepository nextOfKinRepo;
    private final DependantRepository dependantRepo;

    public PrincipalMemberService(
            PrincipalMemberRepository principalRepo,
            NextOfKinRepository nextOfKinRepo,
            DependantRepository dependantRepo) {
        this.principalRepo = principalRepo;
        this.nextOfKinRepo = nextOfKinRepo;
        this.dependantRepo = dependantRepo;
    }

    // ----------------- Helper Methods -----------------

    /** Fetch Principal Member or throw if not found */
    private PrincipalMemberModel getPrincipalOrThrow(Long id) {
        return principalRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Principal Member not found"));
    }

    /** Fetch Dependant or throw if not found */
    private DependantModel getDependantOrThrow(Long id) {
        return dependantRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Dependant not found"));
    }

    /** Validate that a Principal Member does not exceed 6 dependants */
    private void validateDependantLimit(Long principalId) {
        List<DependantModel> existingDependants = dependantRepo.findByPrincipalMemberId(principalId);
        if (existingDependants.size() >= 6) {
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
     * Register a full Principal Member aggregate:
     * - Principal Member (mandatory)
     * - Next of Kin (mandatory)
     * - Dependants (optional, max 6)
     */
    @Transactional
    public MemberDetailsDTO registerFullMember(RegisterMemberRequestDTO request) {
        PrincipalMemberModel principal = request.getPrincipal().toEntity();

        if (request.getNextOfKin() == null) {
            throw new IllegalArgumentException("Next Of Kin is mandatory");
        }
        NextOfKinModel kin = request.getNextOfKin().toEntity();
        principal.setNextOfKin(kin);

        PrincipalMemberModel savedPrincipal = principalRepo.save(principal);

        if (request.getDependants() != null) {
            if (request.getDependants().size() > 6) {
                throw new IllegalStateException("Maximum 6 dependants allowed");
            }
            for (DependantDTO dto : request.getDependants()) {
                DependantModel dependant = dto.toEntity();
                dependant.setPrincipalMember(savedPrincipal);
                dependantRepo.save(dependant);
            }
        }

        return getFullMemberDetails(savedPrincipal.getId());
    }

    /**
     * Retrieve full details of a Principal Member,
     * including Next of Kin and Dependants.
     */
    @Transactional
    public MemberDetailsDTO getFullMemberDetails(Long id) {
        PrincipalMemberModel principal = getPrincipalOrThrow(id);

        NextOfKinDTO kinDTO = principal.getNextOfKin() != null
                ? new NextOfKinDTO(principal.getNextOfKin())
                : null;

        List<DependantDTO> dependantDTOs = dependantRepo.findByPrincipalMemberId(id)
                .stream()
                .map(DependantDTO::new)
                .toList();

        return new MemberDetailsDTO(
                new PrincipalMemberDTO(principal),
                kinDTO != null ? List.of(kinDTO) : List.of(),
                dependantDTOs
        );
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
        return new PrincipalMemberDTO(principalRepo.save(existing));
    }

    /** Partial update of Principal Member (PATCH-style) */
    @Transactional
    public PrincipalMemberDTO patchPrincipal(Long id, PrincipalMemberDTO dto) {
        PrincipalMemberModel existing = getPrincipalOrThrow(id);
        applyPrincipalPatch(existing, dto);
        return new PrincipalMemberDTO(principalRepo.save(existing));
    }

    /** Full update of Next of Kin (PUT-style) */
    @Transactional
    public NextOfKinDTO updateNextOfKin(Long principalId, NextOfKinDTO dto) {
        PrincipalMemberModel principal = getPrincipalOrThrow(principalId);
        NextOfKinModel updatedKin = dto.toEntity();
        principal.setNextOfKin(updatedKin);
        PrincipalMemberModel saved = principalRepo.save(principal);
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
        return new DependantDTO(savedDependant);
    }

    /** Partial update of a Dependant (PATCH-style) */
    @Transactional
    public DependantDTO patchDependant(Long dependantId, DependantDTO dto) {
        DependantModel dependant = getDependantOrThrow(dependantId);
        applyDependantPatch(dependant, dto);
        return new DependantDTO(dependantRepo.save(dependant));
    }

    /** Delete a Principal Member (cascade deletes Next of Kin and Dependants) */
    @Transactional
    public void deletePrincipal(Long id) {
        if (!principalRepo.existsById(id)) {
            throw new RuntimeException("Member not found");
        }
        principalRepo.deleteById(id);
    }

/**
 * Delete a dependant through its Principal Member.
 * Ensures
 */
@Transactional
public void deleteDependant(Long principalId, Long dependantId) {
    PrincipalMemberModel principal = getPrincipalOrThrow(principalId);
    DependantModel dependant = getDependantOrThrow(dependantId);

    if (!dependant.getPrincipalMember().getId().equals(principal.getId())) {
        throw new IllegalStateException("Dependant does not belong to this Principal Member");
    }

    principal.getDependants().remove(dependant);
    principalRepo.save(principal); // orphanRemoval triggers delete
}
}

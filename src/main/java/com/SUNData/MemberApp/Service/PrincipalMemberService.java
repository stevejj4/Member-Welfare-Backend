package com.SUNData.MemberApp.Service;

import com.SUNData.MemberApp.DTOs.*;
import com.SUNData.MemberApp.Model.*;
import com.SUNData.MemberApp.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * REGISTER FULL AGGREGATE
     */
    @Transactional
    public MemberDetailsDTO registerFullMember(RegisterMemberRequestDTO request) {

        // 1️⃣ Build Principal
        PrincipalMemberModel principal = request.getPrincipal().toEntity();

        // 2️⃣ Build and attach NextOfKin (mandatory)
        if (request.getNextOfKin() == null) {
            throw new IllegalArgumentException("Next Of Kin is mandatory");
        }
        NextOfKinModel kin = request.getNextOfKin().toEntity();
        principal.setNextOfKin(kin);

        // 3️⃣ Save Principal (cascade will persist NextOfKin)
        PrincipalMemberModel savedPrincipal = principalRepo.save(principal);

        // 4️⃣ Save Dependants (optional)
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
     * GET FULL MEMBER DETAILS
     */
    @Transactional
    public MemberDetailsDTO getFullMemberDetails(Long id) {

        PrincipalMemberModel principal = principalRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // Fetch Next of Kin directly from the principal
        NextOfKinDTO kinDTO = null;
        if (principal.getNextOfKin() != null) {
            kinDTO = new NextOfKinDTO(principal.getNextOfKin());
        }

        // Fetch dependants via dependantRepo
        List<DependantDTO> dependantDTOs = dependantRepo
                .findByPrincipalMemberId(id)
                .stream()
                .map(DependantDTO::new)
                .toList();

        return new MemberDetailsDTO(
                new PrincipalMemberDTO(principal),
                kinDTO != null ? List.of(kinDTO) : List.of(),
                dependantDTOs
        );
    }
    /**
     * UPDATE PRINCIPAL ONLY
     */
    @Transactional
    public PrincipalMemberDTO updatePrincipal(Long id, PrincipalMemberDTO dto) {

        PrincipalMemberModel existing = principalRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setNationalID(dto.getNationalID());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setGroupName(dto.getGroupName());

        return new PrincipalMemberDTO(principalRepo.save(existing));
    }

    /**
     * DELETE PRINCIPAL
     */
    @Transactional
    public void deletePrincipal(Long id) {

        if (!principalRepo.existsById(id)) {
            throw new RuntimeException("Member not found");
        }

        principalRepo.deleteById(id);
    }
}
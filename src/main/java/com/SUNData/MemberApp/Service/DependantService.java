package com.SUNData.MemberApp.Service;

import com.SUNData.MemberApp.Model.DependantModel;
import com.SUNData.MemberApp.Model.PrincipalMemberModel;
import com.SUNData.MemberApp.Repository.DependantRepository;
import com.SUNData.MemberApp.Repository.PrincipalMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DependantService {

    private final DependantRepository dependantRepo;
    private final PrincipalMemberRepository principalMemberRepo;

    public DependantService(DependantRepository dependantRepo, PrincipalMemberRepository principalMemberRepository) {
        this.dependantRepo = dependantRepo;
        this.principalMemberRepo = principalMemberRepository;
    }

    public List<DependantModel> getDependantsByPrincipalMember(Long memberId) {
        return dependantRepo.findByPrincipalMemberId(memberId);
    }

    public DependantModel addDependant(Long memberId, DependantModel dependant) {

        PrincipalMemberModel member = principalMemberRepo.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Principal Member not found"));
        long count = dependantRepo.countByPrincipalMemberId(memberId);
        if (count >= 6) {
            throw new IllegalStateException("A principal member can have at most 6 dependants");
        }

        dependant.setPrincipalMember(member);
        return dependantRepo.save(dependant);
    }

    public DependantModel updateDependant(Long memberID, Long dependantId, DependantModel updated) {
        DependantModel existing = dependantRepo.findById(dependantId)
                .orElseThrow(() -> new RuntimeException("Dependant not found"));
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setDateOfBirth(updated.getDateOfBirth());
        existing.setRelationship(updated.getRelationship());
        existing.setGender(updated.getGender());
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setBirthCertificatePath(updated.getBirthCertificatePath());
        return dependantRepo.save(existing);
    }

    public void deleteDependant(Long dependantId) {
        dependantRepo.deleteById(dependantId);
    }
}
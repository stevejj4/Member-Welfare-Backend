package com.SUNData.MemberApp.Service;

import com.SUNData.MemberApp.Model.NextOfKinModel;
import com.SUNData.MemberApp.Model.PrincipalMemberModel;
import com.SUNData.MemberApp.Repository.NextOfKinRepository;
import com.SUNData.MemberApp.Repository.PrincipalMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NextOfKinService {

    private final NextOfKinRepository nextOfKinRepository;
    private final PrincipalMemberRepository principalMemberRepository;

    public NextOfKinService(NextOfKinRepository nextOfKinRepository, PrincipalMemberRepository principalMemberRepository) {
        this.nextOfKinRepository = nextOfKinRepository;
        this.principalMemberRepository = principalMemberRepository;
    }

    @Transactional(readOnly = true)
    public NextOfKinModel getNextOfKinByPrincipalMember(Long principalMemberId) {
        PrincipalMemberModel member = principalMemberRepository.findById(principalMemberId)
                .orElseThrow(() -> new RuntimeException("Principal member not found"));

        return member.getNextOfKin();
    }

    @Transactional
    public NextOfKinModel addNextOfKin(Long principalMemberId, NextOfKinModel nextOfKin) {
        PrincipalMemberModel member = principalMemberRepository.findById(principalMemberId)
                .orElseThrow(() -> new RuntimeException("Cannot add Kin: Principal Member not found"));

        // Business Rule: Only one Next of Kin allowed
        if (member.getNextOfKin() != null) {
            throw new IllegalStateException("Member already has a Next of Kin assigned.");
        }

        // Assign the Next of Kin to the principal member
        member.setNextOfKin(nextOfKin);

        // Save both sides of the relationship
        principalMemberRepository.save(member);

        return nextOfKin;
    }


    @Transactional
    public NextOfKinModel updateNextOfKin(Long kinId, NextOfKinModel updated) {
        NextOfKinModel existing = nextOfKinRepository.findById(kinId)
                .orElseThrow(() -> new RuntimeException("Next of Kin record not found"));

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setRelationship(updated.getRelationship());
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setIdNumber(updated.getIdNumber());
        existing.setDateOfBirth(updated.getDateOfBirth());

        return nextOfKinRepository.save(existing);
    }

    @Transactional
    public void deleteNextOfKin(Long id) {
        if (!nextOfKinRepository.existsById(id)) {
            throw new RuntimeException("Next of Kin not found");
        }
        nextOfKinRepository.deleteById(id);
    }
}

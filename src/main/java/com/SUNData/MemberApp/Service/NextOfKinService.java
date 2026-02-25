package com.SUNData.MemberApp.Service;

import com.SUNData.MemberApp.Model.NextOfKinModel;
import com.SUNData.MemberApp.Model.PrincipalMemberModel;
import com.SUNData.MemberApp.Repository.NextOfKinRepository;
import com.SUNData.MemberApp.Repository.PrincipalMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NextOfKinService {

    private final NextOfKinRepository nextOfKinRepository;
    private final PrincipalMemberRepository principalMemberRepository;

    public NextOfKinService(NextOfKinRepository nextOfKinRepository,
                            PrincipalMemberRepository principalMemberRepository) {
        this.nextOfKinRepository = nextOfKinRepository;
        this.principalMemberRepository = principalMemberRepository;
    }

    public List<NextOfKinModel> getAllNextOfKin() {
        return nextOfKinRepository.findAll();
    }

    public List<NextOfKinModel> getNextOfKinByPrincipalMember(Long principalMemberId) {
        return nextOfKinRepository.findByPrincipalMemberId(principalMemberId);
    }

    public Optional<NextOfKinModel> getNextOfKinById(Long id) {
        return nextOfKinRepository.findById(id);
    }

    public NextOfKinModel addNextOfKin(NextOfKinModel nextOfKin) {
        return nextOfKinRepository.save(nextOfKin);
    }

    // Facilitator method: only add if none exists
    public NextOfKinModel addNextOfKinIfNone(Long principalMemberId, NextOfKinModel nextOfKin) {
        List<NextOfKinModel> existingKin = nextOfKinRepository.findByPrincipalMemberId(principalMemberId);

        if (existingKin.isEmpty()) {
            // ✅ Fetch the actual member from DB instead of creating a stub
            PrincipalMemberModel member = principalMemberRepository.findById(principalMemberId)
                    .orElseThrow(() -> new RuntimeException("Principal member not found with id " + principalMemberId));

            nextOfKin.setPrincipalMember(member);
            return nextOfKinRepository.save(nextOfKin);
        } else {
            throw new RuntimeException("Member already has a next of kin.");
        }
    }

    public NextOfKinModel updateNextOfKin(Long id, NextOfKinModel updated) {
        return nextOfKinRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(updated.getFirstName());
                    existing.setLastName(updated.getLastName());
                    existing.setRelationship(updated.getRelationship());
                    existing.setIdNumber(updated.getIdNumber());
                    existing.setPhoneNumber(updated.getPhoneNumber());
                    existing.setDateOfBirth(updated.getDateOfBirth());
                    existing.setIdAttachmentPath(updated.getIdAttachmentPath());
                    return nextOfKinRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Next of kin not found with id " + id));
    }

    public void deleteNextOfKin(Long id) {
        nextOfKinRepository.deleteById(id);
    }
}
package com.SUNData.MemberApp.Service;

import com.SUNData.MemberApp.Model.PrincipalMemberModel;
import com.SUNData.MemberApp.Repository.PrincipalMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PrincipalMemberService {

    private final PrincipalMemberRepository principalMemberRepository;

    public PrincipalMemberService(PrincipalMemberRepository principalMemberRepository) {
        this.principalMemberRepository = principalMemberRepository;
    }

    // CREATE
    public PrincipalMemberModel addMember(PrincipalMemberModel memberData) {
        return principalMemberRepository.save(memberData);
    }

    // READ all
    public List<PrincipalMemberModel> getAllMembers() {
        return principalMemberRepository.findAll();
    }

    // READ by ID
    public Optional<PrincipalMemberModel> getMemberById(Long id) {
        return principalMemberRepository.findById(id);
    }

    // UPDATE
    public PrincipalMemberModel updateMember(Long id, PrincipalMemberModel updated) {
        return principalMemberRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(updated.getFirstName());
                    existing.setLastName(updated.getLastName());
                    existing.setDateOfBirth(updated.getDateOfBirth());
                    existing.setNationalID(updated.getNationalID());
                    existing.setPhoneNumber(updated.getPhoneNumber());
                    existing.setGroupName(updated.getGroupName());
                    return principalMemberRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Member not found with id " + id));
    }

    // DELETE
    public void deleteMember(Long id) {
        principalMemberRepository.deleteById(id);
    }
}
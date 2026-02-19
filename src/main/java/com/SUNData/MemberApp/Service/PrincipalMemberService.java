package com.SUNData.MemberApp.Service;

import com.SUNData.MemberApp.Model.PrincipalMemberModel;
import com.SUNData.MemberApp.Repository.PrincipalMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PrincipalMemberService {

    private PrincipalMemberRepository principalMemberRepository; // Dependancy injection for Data persistance

    public PrincipalMemberService(PrincipalMemberRepository principalMemberRepository) {
        this.principalMemberRepository = principalMemberRepository;
    }
    public List<PrincipalMemberModel> getAllMembers() {
        return principalMemberRepository.findAll();
    }

    public PrincipalMemberModel addMember(PrincipalMemberModel memberData) {
        return principalMemberRepository.save(memberData);
    }
}

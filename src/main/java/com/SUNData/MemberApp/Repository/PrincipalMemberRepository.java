package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.PrincipalMemberModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrincipalMemberRepository extends JpaRepository<PrincipalMemberModel, Long> {

    Optional<PrincipalMemberModel> findById(Long id);

}

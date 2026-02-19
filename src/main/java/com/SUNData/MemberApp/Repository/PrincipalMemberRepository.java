package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.PrincipalMemberModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrincipalMemberRepository extends JpaRepository<PrincipalMemberModel, Long> {
}

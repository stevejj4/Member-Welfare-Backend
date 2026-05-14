package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.MemberModel.DependantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DependantRepository extends JpaRepository<DependantModel, Long> {
    List<DependantModel> findByPrincipalMemberId(Long principalMemberId);
    long countByPrincipalMemberId(Long principalMemberId);

    @Query("SELECT d FROM DependantModel d WHERE d.principalMember.id = :principalId AND d.relationship = 'Son'")
    List<DependantModel> findSonsByPrincipal(Long principalId);
}

package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.DependantModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DependantRepository extends JpaRepository<DependantModel, Long> {
    List<DependantModel> findByPrincipalMemberId(Long principalMemberId);
    long countByPrincipalMemberId(Long principalMemberId);
}

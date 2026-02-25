package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.NextOfKinModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NextOfKinRepository extends JpaRepository<NextOfKinModel, Long> {
    // Optional: find all next of kin for a given principal member
    List<NextOfKinModel> findByPrincipalMemberId(Long principalMemberId);
}
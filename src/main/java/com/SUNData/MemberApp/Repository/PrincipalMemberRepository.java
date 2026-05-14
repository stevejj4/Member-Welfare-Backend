package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.MemberModel.PrincipalMemberModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrincipalMemberRepository extends JpaRepository<PrincipalMemberModel, Long> {
    boolean existsByNationalID(String nationalID);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<PrincipalMemberModel> findByNationalID(String nationalID);

}

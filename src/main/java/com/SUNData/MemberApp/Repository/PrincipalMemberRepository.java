package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.MemberModel.PrincipalMemberModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PrincipalMemberRepository extends JpaRepository<PrincipalMemberModel, Long> {

    // Check if a principal member exists by national ID
    boolean existsByNationalID(String nationalID);

    // Check if a principal member exists by phone number
    boolean existsByPhoneNumber(String phoneNumber);

    // Find a principal member by national ID
    Optional<PrincipalMemberModel> findByNationalID(String nationalID);

    // Find principal members by group name (useful for group-based queries)
    List<PrincipalMemberModel> findByGroupName(String groupName);

    // Optional: search by first and last name together
    Optional<PrincipalMemberModel> findByFirstNameAndLastName(String firstName, String lastName);
}

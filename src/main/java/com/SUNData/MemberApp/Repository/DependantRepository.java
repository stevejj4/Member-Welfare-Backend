package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Enums.RelationshipType;
import com.SUNData.MemberApp.Model.MemberModel.DependantModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DependantRepository extends JpaRepository<DependantModel, Long> {

    // Fetch all dependants for a given principal member
    List<DependantModel> findByPrincipalMemberId(Long principalMemberId);

    // Count dependants for a given principal member (useful for enforcing max 6 dependants rule)
    long countByPrincipalMemberId(Long principalMemberId);

    // Fetch dependants by relationship type (enum-driven, type-safe)
    List<DependantModel> findByPrincipalMemberIdAndRelationship(Long principalId, RelationshipType relationship);

    // Convenience methods for common relationships
    default List<DependantModel> findSonsByPrincipal(Long principalId) {
        return findByPrincipalMemberIdAndRelationship(principalId, RelationshipType.SON);
    }

    default List<DependantModel> findDaughtersByPrincipal(Long principalId) {
        return findByPrincipalMemberIdAndRelationship(principalId, RelationshipType.DAUGHTER);
    }

    default List<DependantModel> findSpouseByPrincipal(Long principalId) {
        return findByPrincipalMemberIdAndRelationship(principalId, RelationshipType.SPOUSE);
    }
}

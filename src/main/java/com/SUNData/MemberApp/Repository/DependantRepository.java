package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Enums.RelationshipType;
import com.SUNData.MemberApp.Model.MemberModel.DependantModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing {@link DependantModel} entities.
 * <p>
 * Provides CRUD operations and custom query methods for dependants
 * associated with a principal member. This repository leverages
 * Spring Data JPA to simplify data access and enforce business rules
 * such as maximum dependants per principal member.
 */
public interface DependantRepository extends JpaRepository<DependantModel, Long> {

    /**
     * Retrieves all dependants belonging to a specific principal member.
     *
     * @param principalMemberId the unique identifier of the principal member
     * @return list of dependants associated with the given principal member
     */
    List<DependantModel> findByPrincipalMemberId(Long principalMemberId);

    /**
     * Counts the number of dependants registered under a specific principal member.
     * <p>
     * Useful for enforcing business rules such as limiting the maximum
     * number of dependants (e.g., 6 per principal member).
     *
     * @param principalMemberId the unique identifier of the principal member
     * @return total number of dependants for the given principal member
     */
    long countByPrincipalMemberId(Long principalMemberId);

    /**
     * Retrieves dependants for a principal member filtered by relationship type.
     * <p>
     * Uses {@link RelationshipType} enum for type safety, avoiding string-based queries.
     *
     * @param principalId the unique identifier of the principal member
     * @param relationship the relationship type (e.g., SON, DAUGHTER, SPOUSE)
     * @return list of dependants matching the given relationship type
     */
    List<DependantModel> findByPrincipalMemberIdAndRelationship(Long principalId, RelationshipType relationship);

    /**
     * Convenience method to fetch all sons of a given principal member.
     *
     * @param principalId the unique identifier of the principal member
     * @return list of dependants with relationship type SON
     */
    default List<DependantModel> findSonsByPrincipal(Long principalId) {
        return findByPrincipalMemberIdAndRelationship(principalId, RelationshipType.SON);
    }

    /**
     * Convenience method to fetch all daughters of a given principal member.
     *
     * @param principalId the unique identifier of the principal member
     * @return list of dependants with relationship type DAUGHTER
     */
    default List<DependantModel> findDaughtersByPrincipal(Long principalId) {
        return findByPrincipalMemberIdAndRelationship(principalId, RelationshipType.DAUGHTER);
    }

    /**
     * Convenience method to fetch the spouse of a given principal member.
     *
     * @param principalId the unique identifier of the principal member
     * @return list of dependants with relationship type SPOUSE
     */
    default List<DependantModel> findSpouseByPrincipal(Long principalId) {
        return findByPrincipalMemberIdAndRelationship(principalId, RelationshipType.SPOUSE);
    }
}

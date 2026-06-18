package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.MemberModel.PrincipalMemberModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

/**
 * Repository interface for managing {@link PrincipalMemberModel} entities.
 * <p>
 * Provides CRUD operations and custom query methods for principal members.
 * This repository supports validation of unique identifiers (national ID, phone number),
 * group-based queries, and name-based lookups.
 */
public interface PrincipalMemberRepository extends JpaRepository<PrincipalMemberModel, Long> {

    /**
     * Checks if a principal member exists with the given national ID.
     * <p>
     * Useful for enforcing uniqueness constraints before persisting a new member.
     *
     * @param nationalID the national ID to check
     * @return true if a principal member exists with the given national ID, false otherwise
     */
    boolean existsByNationalID(String nationalID);

    /**
     * Checks if a principal member exists with the given phone number.
     * <p>
     * Useful for enforcing uniqueness constraints before persisting a new member.
     *
     * @param phoneNumber the phone number to check
     * @return true if a principal member exists with the given phone number, false otherwise
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Finds a principal member by their national ID.
     *
     * @param nationalID the national ID of the principal member
     * @return an {@link Optional} containing the principal member if found,
     *         or empty if no member exists with the given national ID
     */
    Optional<PrincipalMemberModel> findByNationalID(String nationalID);

    /**
     * Retrieves all principal members belonging to a specific group.
     * <p>
     * Useful for group-based queries, such as county or community membership.
     *
     * @param groupName the name of the group
     * @return list of principal members associated with the given group name
     */
    List<PrincipalMemberModel> findByGroupName(String groupName);

    /**
     * Finds a principal member by their first and last name.
     * <p>
     * Useful for quick lookups when national ID or phone number is not available.
     *
     * @param firstName the first name of the principal member
     * @param lastName  the last name of the principal member
     * @return an {@link Optional} containing the principal member if found,
     *         or empty if no member exists with the given names
     */
    Optional<PrincipalMemberModel> findByFirstNameAndLastName(String firstName, String lastName);
}

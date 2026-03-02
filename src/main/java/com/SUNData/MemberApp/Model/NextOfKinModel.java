package com.SUNData.MemberApp.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "next_of_kin")
public class NextOfKinModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String relationship; // e.g., spouse, sibling, parent

    @Column(nullable = false)
    private String idNumber; // removed unique constraint to allow sharing

    private String phoneNumber;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    // Option 1: store file path
    private String idAttachmentPath;

    // Relationship with Principal Members (shared Next of Kin)
    // orphanRemoval = true -- ensures principal member removal, nextOfKin is also removed
    @OneToMany(mappedBy = "nextOfKin", orphanRemoval = true)
    private List<PrincipalMemberModel> principalMembers;

    public NextOfKinModel() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getIdAttachmentPath() { return idAttachmentPath; }
    public void setIdAttachmentPath(String idAttachmentPath) { this.idAttachmentPath = idAttachmentPath; }

    public List<PrincipalMemberModel> getPrincipalMembers() { return principalMembers; }
    public void setPrincipalMembers(List<PrincipalMemberModel> principalMembers) { this.principalMembers = principalMembers; }
}
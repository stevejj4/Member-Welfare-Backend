package com.SUNData.MemberApp.Model.MemberModel;

import com.SUNData.MemberApp.Enums.GenderType;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "principal_member")
public class PrincipalMemberModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(nullable = false, unique = true) // enforce uniqueness for national ID
    private String nationalID;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private String groupName;

    @CreationTimestamp
    private LocalDateTime registrationDate;

    @Column(name = "registered_by_name")
    private String registeredByName;

    @Column(name = "registered_by_role")
    private String registeredByRole;

    // Each principal member has exactly one next of kin
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "next_of_kin_id", nullable = false)
    private NextOfKinModel nextOfKin;

    // Each principal member can have many dependants
    @OneToMany(mappedBy = "principalMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DependantModel> dependants;

    public PrincipalMemberModel() {}

    public PrincipalMemberModel(Long id) {
        this.id = id;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public String getNationalID() { return nationalID; }
    public void setNationalID(String nationalID) { this.nationalID = nationalID; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public NextOfKinModel getNextOfKin() { return nextOfKin; }
    public void setNextOfKin(NextOfKinModel nextOfKin) { this.nextOfKin = nextOfKin; }

    public List<DependantModel> getDependants() { return dependants; }
    public void setDependants(List<DependantModel> dependants) { this.dependants = dependants; }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegisteredByName() {
        return registeredByName;
    }

    public void setRegisteredByName(String registeredByName) {
        this.registeredByName = registeredByName;
    }

    public String getRegisteredByRole() {
        return registeredByRole;
    }

    public void setRegisteredByRole(String registeredByRole) {
        this.registeredByRole = registeredByRole;
    }
}
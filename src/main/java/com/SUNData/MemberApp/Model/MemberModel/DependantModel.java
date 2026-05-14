package com.SUNData.MemberApp.Model.MemberModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Entity
@Table (name = "Dependants")
@NoArgsConstructor
@AllArgsConstructor

public class DependantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank
    private String relationship; // "Son", "Daughter", "Spouse"

    @NotBlank
    private String gender;

    private String phoneNumber;

    private  String birthCertificatePath; // file upload path

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "principal_member_id")
    private PrincipalMemberModel principalMember;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public @NotBlank String getRelationship() {
        return relationship;
    }

    public void setRelationship(@NotNull String relationship) {
        this.relationship = String.valueOf(relationship);
    }

    public @NotBlank String getGender() {
        return gender;
    }

    public void setGender(@NotBlank String gender) {
        this.gender = gender;
    }

    public @NotBlank String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthCertificatePath() {
        return birthCertificatePath;
    }

    public void setBirthCertificatePath(String birthCertificatePath) {
        this.birthCertificatePath = birthCertificatePath;
    }

    public PrincipalMemberModel getPrincipalMember() {
        return principalMember;
    }

    public void setPrincipalMember(PrincipalMemberModel principalMember) {
        this.principalMember = principalMember;
    }
}

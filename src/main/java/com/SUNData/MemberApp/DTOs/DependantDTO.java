package com.SUNData.MemberApp.DTOs;

import com.SUNData.MemberApp.Model.DependantModel;

import java.time.LocalDate;

public class DependantDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String relationship;
    private String gender;
    private String phoneNumber;
    private String birthCertificatePath;

    public DependantDTO(DependantModel model) {
        this.id = model.getId();
        this.firstName = model.getFirstName();
        this.lastName = model.getLastName();
        this.dateOfBirth = model.getDateOfBirth();
        this.relationship = model.getRelationship();
        this.gender = model.getGender();
        this.phoneNumber = model.getPhoneNumber();
        this.birthCertificatePath = model.getBirthCertificatePath();
    }

    // getters only


    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBirthCertificatePath() {
        return birthCertificatePath;
    }
}
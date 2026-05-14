package com.SUNData.MemberApp.DTOs.Member;

import com.SUNData.MemberApp.Model.MemberModel.DependantModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class DependantDTO {
    private Long id;
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    private String gender; // Later: I will replace with GenderType enum

    @NotNull(message = "Relationship is required")
    private String relationship;

    // phone number is optional: will be validated if present
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    private String birthCertificatePath;

    public DependantDTO(){}

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
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }

    public @NotNull(message = "Relationship is required") String getRelationship() {
        return relationship;
    }

    public String getGender() { return gender; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getBirthCertificatePath() { return birthCertificatePath; }

    // Convert DTO back to entity
    public DependantModel toEntity() {
        DependantModel model = new DependantModel();

        if (this.id != null) {
            model.setId(this.id);
        }

        model.setFirstName(this.firstName);
        model.setLastName(this.lastName);
        model.setDateOfBirth(this.dateOfBirth);
        model.setRelationship(this.relationship);
        model.setGender(this.gender);
        model.setPhoneNumber(this.phoneNumber);
        model.setBirthCertificatePath(this.birthCertificatePath);

        return model;
    }

}
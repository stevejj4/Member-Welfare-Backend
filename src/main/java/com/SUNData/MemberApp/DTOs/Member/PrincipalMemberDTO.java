package com.SUNData.MemberApp.DTOs.Member;

import com.SUNData.MemberApp.Enums.GenderType;
import com.SUNData.MemberApp.Model.MemberModel.PrincipalMemberModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class PrincipalMemberDTO {
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "National ID is required")
    private String nationalID;

    @NotNull(message = "")
    private GenderType gender;

    @NotBlank(message = "Principal member must have a phone number")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    private String groupName;
    @NotNull(message = "Principal member must have date of birth")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;


    // Default constructor for Jackson
    public PrincipalMemberDTO() {}


    public PrincipalMemberDTO(PrincipalMemberModel model) {
        this.id = model.getId();
        this.firstName = model.getFirstName();
        this.lastName = model.getLastName();
        this.nationalID = model.getNationalID();
        this.gender = model.getGender();
        this.phoneNumber = model.getPhoneNumber();
        this.groupName = model.getGroupName();
        this.dateOfBirth = model.getDateOfBirth(); // <-- map it
    }

    // getters
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getNationalID() { return nationalID; }

    public @NotNull(message = "") GenderType getGender() {
        return gender;
    }

    public String getPhoneNumber() { return phoneNumber; }
    public String getGroupName() { return groupName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; } // <-- getter

    public PrincipalMemberModel toEntity() {
        PrincipalMemberModel model = new PrincipalMemberModel();
        if (this.id != null) model.setId(this.id);
        model.setFirstName(this.firstName);
        model.setLastName(this.lastName);
        model.setNationalID(this.nationalID);
        model.setGender(this.gender);
        model.setPhoneNumber(this.phoneNumber);
        model.setGroupName(this.groupName);
        model.setDateOfBirth(this.dateOfBirth); // <-- set it
        return model;
    }
}
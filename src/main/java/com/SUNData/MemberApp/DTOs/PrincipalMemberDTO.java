package com.SUNData.MemberApp.DTOs;

import com.SUNData.MemberApp.Model.PrincipalMemberModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class PrincipalMemberDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String nationalID;
    private String phoneNumber;
    private String groupName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;


    // Default constructor for Jackson
    public PrincipalMemberDTO() {}


    public PrincipalMemberDTO(PrincipalMemberModel model) {
        this.id = model.getId();
        this.firstName = model.getFirstName();
        this.lastName = model.getLastName();
        this.nationalID = model.getNationalID();
        this.phoneNumber = model.getPhoneNumber();
        this.groupName = model.getGroupName();
        this.dateOfBirth = model.getDateOfBirth(); // <-- map it
    }

    // getters
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getNationalID() { return nationalID; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getGroupName() { return groupName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; } // <-- getter

    public PrincipalMemberModel toEntity() {
        PrincipalMemberModel model = new PrincipalMemberModel();
        if (this.id != null) model.setId(this.id);
        model.setFirstName(this.firstName);
        model.setLastName(this.lastName);
        model.setNationalID(this.nationalID);
        model.setPhoneNumber(this.phoneNumber);
        model.setGroupName(this.groupName);
        model.setDateOfBirth(this.dateOfBirth); // <-- set it
        return model;
    }
}
package com.SUNData.MemberApp.DTOs;

import com.SUNData.MemberApp.Model.PrincipalMemberModel;

public class PrincipalMemberDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String nationalID;
    private String phoneNumber;
    private String groupName;

    public PrincipalMemberDTO(PrincipalMemberModel model) {
        this.id = model.getId();
        this.firstName = model.getFirstName();
        this.lastName = model.getLastName();
        this.nationalID = model.getNationalID();
        this.phoneNumber = model.getPhoneNumber();
        this.groupName = model.getGroupName();
    }

    // getters only
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getNationalID() { return nationalID; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getGroupName() { return groupName; }
}

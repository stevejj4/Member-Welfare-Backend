package com.SUNData.MemberApp.DTOs.User;

import com.SUNData.MemberApp.Enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class UpdateUserRequestDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private UserRole assignedRole;

    private Long countyId;

    private Long subCountyId;

    private List<Long> wardIds;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getAssignedRole() {
        return assignedRole;
    }

    public void setAssignedRole(UserRole assignedRole) {
        this.assignedRole = assignedRole;
    }

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public Long getSubCountyId() {
        return subCountyId;
    }

    public void setSubCountyId(Long subCountyId) {
        this.subCountyId = subCountyId;
    }

    public List<Long> getWardIds() {
        return wardIds;
    }

    public void setWardIds(List<Long> wardIds) {
        this.wardIds = wardIds;
    }
}

package com.SUNData.MemberApp.DTOs.Member;


import com.SUNData.MemberApp.Model.MemberModel.NextOfKinModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class NextOfKinDTO {
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "National ID is required")
    private String idNumber;

    @NotBlank(message = "Next of kin must have phone number")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotNull(message = "Relationship is required")
    private String relationship;

    @NotNull(message = "Date of birth is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String idAttachmentPath;

    // Default constructor for Jackson
    public NextOfKinDTO() {}

    // Entity → DTO constructor
    public NextOfKinDTO(NextOfKinModel model) {
        this.id = model.getId();
        this.firstName = model.getFirstName();
        this.lastName = model.getLastName();
        this.relationship = model.getRelationship();
        this.idNumber = model.getIdNumber();
        this.phoneNumber = model.getPhoneNumber();
        this.dateOfBirth = model.getDateOfBirth();
        this.idAttachmentPath = model.getIdAttachmentPath();
    }

    // getters
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getRelationship() { return relationship; }
    public String getIdNumber() { return idNumber; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getIdAttachmentPath() { return idAttachmentPath; }

    // Convert DTO back to entity
    public NextOfKinModel toEntity() {
        NextOfKinModel model = new NextOfKinModel();
        if (this.id != null) model.setId(this.id);
        model.setFirstName(this.firstName);
        model.setLastName(this.lastName);
        model.setRelationship(String.valueOf(this.relationship));
        model.setIdNumber(this.idNumber);
        model.setPhoneNumber(this.phoneNumber);
        model.setDateOfBirth(this.dateOfBirth);
        model.setIdAttachmentPath(this.idAttachmentPath);
        return model;
    }
}
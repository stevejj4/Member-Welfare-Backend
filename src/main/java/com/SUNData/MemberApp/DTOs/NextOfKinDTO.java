package com.SUNData.MemberApp.DTOs;

import com.SUNData.MemberApp.Model.NextOfKinModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class NextOfKinDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String relationship;
    private String idNumber;
    private String phoneNumber;
    @JsonFormat(pattern = "dd-MM-yyyy")
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
        model.setRelationship(this.relationship);
        model.setIdNumber(this.idNumber);
        model.setPhoneNumber(this.phoneNumber);
        model.setDateOfBirth(this.dateOfBirth);
        model.setIdAttachmentPath(this.idAttachmentPath);
        return model;
    }
}
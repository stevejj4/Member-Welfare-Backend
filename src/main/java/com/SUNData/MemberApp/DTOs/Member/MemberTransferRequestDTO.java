package com.SUNData.MemberApp.DTOs.Member;

import com.SUNData.MemberApp.Enums.RegistrationType;
import com.SUNData.MemberApp.Enums.TransferRequestStatus;
import com.SUNData.MemberApp.Model.MemberModel.MemberTransferRequestModel;

import java.time.LocalDateTime;

public class MemberTransferRequestDTO {
    private Long id;
    private Long memberId;
    private String memberName;
    private String nationalID;
    private String phoneNumber;
    private RegistrationType registrationType;
    private TransferRequestStatus status;
    private Long countyId;
    private String countyName;
    private Long subCountyId;
    private String subCountyName;
    private Long wardId;
    private String wardName;
    private Long groupId;
    private String groupName;
    private String reason;
    private String requestedByName;
    private LocalDateTime requestedAt;

    public MemberTransferRequestDTO(MemberTransferRequestModel model) {
        this.id = model.getId();
        this.memberId = model.getPrincipalMember().getId();
        this.memberName = model.getPrincipalMember().getFirstName() + " " + model.getPrincipalMember().getLastName();
        this.nationalID = model.getPrincipalMember().getNationalID();
        this.phoneNumber = model.getPrincipalMember().getPhoneNumber();
        this.registrationType = model.getRegistrationType();
        this.status = model.getStatus();
        this.countyId = model.getTargetCounty().getId();
        this.countyName = model.getTargetCounty().getName();
        this.subCountyId = model.getTargetSubCounty().getId();
        this.subCountyName = model.getTargetSubCounty().getName();
        this.wardId = model.getTargetWard().getId();
        this.wardName = model.getTargetWard().getName();
        if (model.getTargetGroup() != null) {
            this.groupId = model.getTargetGroup().getId();
            this.groupName = model.getTargetGroup().getName();
        }
        this.reason = model.getReason();
        this.requestedByName = model.getRequestedBy() != null ? model.getRequestedBy().getFullName() : null;
        this.requestedAt = model.getRequestedAt();
    }

    public Long getId() { return id; }
    public Long getMemberId() { return memberId; }
    public String getMemberName() { return memberName; }
    public String getNationalID() { return nationalID; }
    public String getPhoneNumber() { return phoneNumber; }
    public RegistrationType getRegistrationType() { return registrationType; }
    public TransferRequestStatus getStatus() { return status; }
    public Long getCountyId() { return countyId; }
    public String getCountyName() { return countyName; }
    public Long getSubCountyId() { return subCountyId; }
    public String getSubCountyName() { return subCountyName; }
    public Long getWardId() { return wardId; }
    public String getWardName() { return wardName; }
    public Long getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public String getReason() { return reason; }
    public String getRequestedByName() { return requestedByName; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
}

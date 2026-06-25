package com.SUNData.MemberApp.DTOs.Group;

import com.SUNData.MemberApp.Enums.TransferRequestStatus;
import com.SUNData.MemberApp.Model.GroupModel.GroupTransferRequestModel;

import java.time.LocalDateTime;

public class GroupTransferRequestDTO {
    private Long id;
    private Long groupId;
    private String groupCode;
    private String groupName;
    private TransferRequestStatus status;
    private Long countyId;
    private String countyName;
    private Long subCountyId;
    private String subCountyName;
    private Long wardId;
    private String wardName;
    private String reason;
    private String requestedByName;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;

    public GroupTransferRequestDTO(GroupTransferRequestModel model) {
        this.id = model.getId();
        this.groupId = model.getGroup().getId();
        this.groupCode = model.getGroup().getGroupId();
        this.groupName = model.getGroup().getName();
        this.status = model.getStatus();
        this.countyId = model.getTargetCounty().getId();
        this.countyName = model.getTargetCounty().getName();
        this.subCountyId = model.getTargetSubCounty().getId();
        this.subCountyName = model.getTargetSubCounty().getName();
        this.wardId = model.getTargetWard().getId();
        this.wardName = model.getTargetWard().getName();
        this.reason = model.getReason();
        this.requestedByName = model.getRequestedBy() != null ? model.getRequestedBy().getFullName() : null;
        this.requestedAt = model.getRequestedAt();
        this.approvedAt = model.getApprovedAt();
    }

    public Long getId() { return id; }
    public Long getGroupId() { return groupId; }
    public String getGroupCode() { return groupCode; }
    public String getGroupName() { return groupName; }
    public TransferRequestStatus getStatus() { return status; }
    public Long getCountyId() { return countyId; }
    public String getCountyName() { return countyName; }
    public Long getSubCountyId() { return subCountyId; }
    public String getSubCountyName() { return subCountyName; }
    public Long getWardId() { return wardId; }
    public String getWardName() { return wardName; }
    public String getReason() { return reason; }
    public String getRequestedByName() { return requestedByName; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
}

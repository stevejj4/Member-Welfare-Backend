package com.SUNData.MemberApp.DTOs.Member;

import jakarta.validation.constraints.NotNull;

public class ApproveMemberTransferRequestDTO {
    @NotNull(message = "Target group is required")
    private Long groupId;

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }
}

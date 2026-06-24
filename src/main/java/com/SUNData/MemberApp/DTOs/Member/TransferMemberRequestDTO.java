package com.SUNData.MemberApp.DTOs.Member;

import com.SUNData.MemberApp.Enums.RegistrationType;
import jakarta.validation.constraints.NotNull;

public class TransferMemberRequestDTO {
    @NotNull(message = "Ward is required")
    private Long wardId;

    private Long groupId;

    private RegistrationType registrationType = RegistrationType.INDIVIDUAL;

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(Long wardId) {
        this.wardId = wardId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public RegistrationType getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationType registrationType) {
        this.registrationType = registrationType;
    }
}

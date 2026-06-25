package com.SUNData.MemberApp.DTOs.Group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateGroupTransferRequestDTO {
    @NotNull(message = "County is required")
    private Long countyId;

    @NotNull(message = "Sub-county is required")
    private Long subCountyId;

    @NotNull(message = "Ward is required")
    private Long wardId;

    @NotBlank(message = "Transfer reason is required")
    private String reason;

    public Long getCountyId() { return countyId; }
    public void setCountyId(Long countyId) { this.countyId = countyId; }
    public Long getSubCountyId() { return subCountyId; }
    public void setSubCountyId(Long subCountyId) { this.subCountyId = subCountyId; }
    public Long getWardId() { return wardId; }
    public void setWardId(Long wardId) { this.wardId = wardId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

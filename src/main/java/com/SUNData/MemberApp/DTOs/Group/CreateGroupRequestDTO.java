package com.SUNData.MemberApp.DTOs.Group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateGroupRequestDTO {

    @NotBlank(message = "Group name is required")
    private String name;

    @NotNull(message = "County is required")
    private Long countyId;

    @NotNull(message = "Sub-county is required")
    private Long subCountyId;

    @NotNull(message = "Ward is required")
    private Long wardId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getCountyId() { return countyId; }
    public void setCountyId(Long countyId) { this.countyId = countyId; }

    public Long getSubCountyId() { return subCountyId; }
    public void setSubCountyId(Long subCountyId) { this.subCountyId = subCountyId; }

    public Long getWardId() { return wardId; }
    public void setWardId(Long wardId) { this.wardId = wardId; }
}

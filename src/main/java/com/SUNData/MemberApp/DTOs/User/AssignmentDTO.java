package com.SUNData.MemberApp.DTOs.User;

import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Model.LocationModel.WardModel;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;

import java.util.List;

public class AssignmentDTO {
    private UserRole role;
    private Long countyId;
    private String regionName;
    private String countyName;
    private Long subCountyId;
    private String subCountyName;
    private List<Long> wardIds;
    private List<String> wardNames;

    public AssignmentDTO() {}

    public AssignmentDTO(SystemUserModel user) {
        this.role = user.getRole();
        if (user.getAssignedCounty() != null) {
            this.countyId = user.getAssignedCounty().getId();
            this.regionName = user.getAssignedCounty().getRegion();
            this.countyName = user.getAssignedCounty().getName();
        }
        if (user.getAssignedSubCounty() != null) {
            this.subCountyId = user.getAssignedSubCounty().getId();
            this.subCountyName = user.getAssignedSubCounty().getName();
        }
        this.wardIds = user.getAssignedWards().stream().map(WardModel::getId).toList();
        this.wardNames = user.getAssignedWards().stream().map(WardModel::getName).toList();
    }

    public UserRole getRole() { return role; }
    public Long getCountyId() { return countyId; }
    public String getRegionName() { return regionName; }
    public String getCountyName() { return countyName; }
    public Long getSubCountyId() { return subCountyId; }
    public String getSubCountyName() { return subCountyName; }
    public List<Long> getWardIds() { return wardIds; }
    public List<String> getWardNames() { return wardNames; }
}

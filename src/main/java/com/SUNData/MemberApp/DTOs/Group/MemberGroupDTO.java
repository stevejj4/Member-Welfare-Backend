package com.SUNData.MemberApp.DTOs.Group;

import com.SUNData.MemberApp.Model.GroupModel.MemberGroupModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class MemberGroupDTO {
    private Long id;
    private String groupId;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateCreated;

    private Long countyId;
    private String countyName;
    private Long subCountyId;
    private String subCountyName;
    private Long wardId;
    private String wardName;

    public MemberGroupDTO() {}

    public MemberGroupDTO(MemberGroupModel group) {
        this.id = group.getId();
        this.groupId = group.getGroupId();
        this.name = group.getName();
        this.dateCreated = group.getDateCreated();
        this.countyId = group.getCounty().getId();
        this.countyName = group.getCounty().getName();
        this.subCountyId = group.getSubCounty().getId();
        this.subCountyName = group.getSubCounty().getName();
        this.wardId = group.getWard().getId();
        this.wardName = group.getWard().getName();
    }

    public Long getId() { return id; }
    public String getGroupId() { return groupId; }
    public String getName() { return name; }
    public LocalDateTime getDateCreated() { return dateCreated; }
    public Long getCountyId() { return countyId; }
    public String getCountyName() { return countyName; }
    public Long getSubCountyId() { return subCountyId; }
    public String getSubCountyName() { return subCountyName; }
    public Long getWardId() { return wardId; }
    public String getWardName() { return wardName; }
}

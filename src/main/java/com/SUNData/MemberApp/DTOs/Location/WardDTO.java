package com.SUNData.MemberApp.DTOs.Location;

import com.SUNData.MemberApp.Model.LocationModel.WardModel;

public class WardDTO {
    private Long id;
    private Long subCountyId;
    private String name;

    public WardDTO() {}

    public WardDTO(WardModel ward) {
        this.id = ward.getId();
        this.subCountyId = ward.getSubCounty().getId();
        this.name = ward.getName();
    }

    public Long getId() {
        return id;
    }

    public Long getSubCountyId() {
        return subCountyId;
    }

    public String getName() {
        return name;
    }
}

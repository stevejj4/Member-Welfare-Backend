package com.SUNData.MemberApp.DTOs.Location;

import com.SUNData.MemberApp.Model.LocationModel.SubCountyModel;

public class SubCountyDTO {
    private Long id;
    private Long countyId;
    private String name;

    public SubCountyDTO() {}

    public SubCountyDTO(SubCountyModel subCounty) {
        this.id = subCounty.getId();
        this.countyId = subCounty.getCounty().getId();
        this.name = subCounty.getName();
    }

    public Long getId() {
        return id;
    }

    public Long getCountyId() {
        return countyId;
    }

    public String getName() {
        return name;
    }
}

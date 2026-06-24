package com.SUNData.MemberApp.DTOs.Location;

import com.SUNData.MemberApp.Model.LocationModel.CountyModel;

public class CountyDTO {
    private Long id;
    private String name;

    public CountyDTO() {}

    public CountyDTO(CountyModel county) {
        this.id = county.getId();
        this.name = county.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

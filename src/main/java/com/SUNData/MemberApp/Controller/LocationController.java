package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Location.CountyDTO;
import com.SUNData.MemberApp.DTOs.Location.SubCountyDTO;
import com.SUNData.MemberApp.DTOs.Location.WardDTO;
import com.SUNData.MemberApp.Service.location.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@PreAuthorize("isAuthenticated()")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/counties")
    public ResponseEntity<List<CountyDTO>> getCounties() {
        return ResponseEntity.ok(locationService.getCounties());
    }

    @GetMapping("/sub-counties")
    public ResponseEntity<List<SubCountyDTO>> getSubCounties(@RequestParam Long countyId) {
        return ResponseEntity.ok(locationService.getSubCounties(countyId));
    }

    @GetMapping("/wards")
    public ResponseEntity<List<WardDTO>> getWards(@RequestParam Long subCountyId) {
        return ResponseEntity.ok(locationService.getWards(subCountyId));
    }
}

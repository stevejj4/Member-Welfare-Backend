package com.SUNData.MemberApp.Service.location;

import com.SUNData.MemberApp.DTOs.Location.CountyDTO;
import com.SUNData.MemberApp.DTOs.Location.SubCountyDTO;
import com.SUNData.MemberApp.DTOs.Location.WardDTO;
import com.SUNData.MemberApp.Repository.CountyRepository;
import com.SUNData.MemberApp.Repository.SubCountyRepository;
import com.SUNData.MemberApp.Repository.WardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {

    private final CountyRepository countyRepository;
    private final SubCountyRepository subCountyRepository;
    private final WardRepository wardRepository;

    public LocationService(
            CountyRepository countyRepository,
            SubCountyRepository subCountyRepository,
            WardRepository wardRepository) {
        this.countyRepository = countyRepository;
        this.subCountyRepository = subCountyRepository;
        this.wardRepository = wardRepository;
    }

    @Transactional(readOnly = true)
    public List<CountyDTO> getCounties() {
        return countyRepository.findAll().stream()
                .map(CountyDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SubCountyDTO> getSubCounties(Long countyId) {
        return subCountyRepository.findByCountyId(countyId).stream()
                .map(SubCountyDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<WardDTO> getWards(Long subCountyId) {
        return wardRepository.findBySubCountyId(subCountyId).stream()
                .map(WardDTO::new)
                .toList();
    }
}

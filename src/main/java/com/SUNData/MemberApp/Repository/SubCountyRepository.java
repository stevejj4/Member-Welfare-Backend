package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.LocationModel.SubCountyModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCountyRepository extends JpaRepository<SubCountyModel, Long> {
    List<SubCountyModel> findByCountyId(Long countyId);
}

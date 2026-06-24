package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.LocationModel.CountyModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountyRepository extends JpaRepository<CountyModel, Long> {
}

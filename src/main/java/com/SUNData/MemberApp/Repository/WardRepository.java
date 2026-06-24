package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.LocationModel.WardModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface WardRepository extends JpaRepository<WardModel, Long> {
    List<WardModel> findBySubCountyId(Long subCountyId);

    List<WardModel> findByIdIn(Collection<Long> ids);
}

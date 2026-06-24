package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.GroupModel.MemberGroupModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MemberGroupRepository extends JpaRepository<MemberGroupModel, Long> {
    List<MemberGroupModel> findByWardId(Long wardId);

    List<MemberGroupModel> findBySubCountyId(Long subCountyId);

    List<MemberGroupModel> findByWardIdIn(Collection<Long> wardIds);
}

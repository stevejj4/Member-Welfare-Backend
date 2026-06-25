package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.GroupModel.GroupTransferRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface GroupTransferRequestRepository extends JpaRepository<GroupTransferRequestModel, Long> {
    List<GroupTransferRequestModel> findAllByOrderByRequestedAtDesc();

    List<GroupTransferRequestModel> findByTargetWard_IdInOrderByRequestedAtDesc(Collection<Long> wardIds);

    List<GroupTransferRequestModel> findByTargetSubCounty_IdOrderByRequestedAtDesc(Long subCountyId);
}

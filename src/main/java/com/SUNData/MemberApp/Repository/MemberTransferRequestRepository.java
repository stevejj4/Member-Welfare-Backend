package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Enums.TransferRequestStatus;
import com.SUNData.MemberApp.Model.MemberModel.MemberTransferRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MemberTransferRequestRepository extends JpaRepository<MemberTransferRequestModel, Long> {
    List<MemberTransferRequestModel> findAllByOrderByRequestedAtDesc();

    List<MemberTransferRequestModel> findByStatusOrderByRequestedAtDesc(TransferRequestStatus status);

    List<MemberTransferRequestModel> findByStatusAndTargetWard_IdInOrderByRequestedAtDesc(
            TransferRequestStatus status,
            Collection<Long> wardIds
    );

    List<MemberTransferRequestModel> findByStatusAndTargetSubCounty_IdOrderByRequestedAtDesc(
            TransferRequestStatus status,
            Long subCountyId
    );

    List<MemberTransferRequestModel> findByTargetWard_IdInOrderByRequestedAtDesc(Collection<Long> wardIds);

    List<MemberTransferRequestModel> findByTargetSubCounty_IdOrderByRequestedAtDesc(Long subCountyId);
}

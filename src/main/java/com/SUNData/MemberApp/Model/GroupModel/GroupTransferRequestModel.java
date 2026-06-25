package com.SUNData.MemberApp.Model.GroupModel;

import com.SUNData.MemberApp.Enums.TransferRequestStatus;
import com.SUNData.MemberApp.Model.LocationModel.CountyModel;
import com.SUNData.MemberApp.Model.LocationModel.SubCountyModel;
import com.SUNData.MemberApp.Model.LocationModel.WardModel;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_transfer_requests")
public class GroupTransferRequestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_group_id", nullable = false)
    private MemberGroupModel group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_county_id", nullable = false)
    private CountyModel targetCounty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_sub_county_id", nullable = false)
    private SubCountyModel targetSubCounty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_ward_id", nullable = false)
    private WardModel targetWard;

    @Column(nullable = false, length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferRequestStatus status = TransferRequestStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by_user_id")
    private SystemUserModel requestedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_user_id")
    private SystemUserModel approvedBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;

    public Long getId() { return id; }
    public MemberGroupModel getGroup() { return group; }
    public void setGroup(MemberGroupModel group) { this.group = group; }
    public CountyModel getTargetCounty() { return targetCounty; }
    public void setTargetCounty(CountyModel targetCounty) { this.targetCounty = targetCounty; }
    public SubCountyModel getTargetSubCounty() { return targetSubCounty; }
    public void setTargetSubCounty(SubCountyModel targetSubCounty) { this.targetSubCounty = targetSubCounty; }
    public WardModel getTargetWard() { return targetWard; }
    public void setTargetWard(WardModel targetWard) { this.targetWard = targetWard; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public TransferRequestStatus getStatus() { return status; }
    public void setStatus(TransferRequestStatus status) { this.status = status; }
    public SystemUserModel getRequestedBy() { return requestedBy; }
    public void setRequestedBy(SystemUserModel requestedBy) { this.requestedBy = requestedBy; }
    public SystemUserModel getApprovedBy() { return approvedBy; }
    public void setApprovedBy(SystemUserModel approvedBy) { this.approvedBy = approvedBy; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
}

package com.SUNData.MemberApp.Model.MemberModel;

import com.SUNData.MemberApp.Enums.RegistrationType;
import com.SUNData.MemberApp.Enums.TransferRequestStatus;
import com.SUNData.MemberApp.Model.GroupModel.MemberGroupModel;
import com.SUNData.MemberApp.Model.LocationModel.CountyModel;
import com.SUNData.MemberApp.Model.LocationModel.SubCountyModel;
import com.SUNData.MemberApp.Model.LocationModel.WardModel;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_transfer_requests")
public class MemberTransferRequestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "principal_member_id", nullable = false)
    private PrincipalMemberModel principalMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_county_id", nullable = false)
    private CountyModel targetCounty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_sub_county_id", nullable = false)
    private SubCountyModel targetSubCounty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_ward_id", nullable = false)
    private WardModel targetWard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_group_id")
    private MemberGroupModel targetGroup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationType registrationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferRequestStatus status = TransferRequestStatus.PENDING;

    @Column(nullable = false, length = 500)
    private String reason;

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
    public PrincipalMemberModel getPrincipalMember() { return principalMember; }
    public void setPrincipalMember(PrincipalMemberModel principalMember) { this.principalMember = principalMember; }
    public CountyModel getTargetCounty() { return targetCounty; }
    public void setTargetCounty(CountyModel targetCounty) { this.targetCounty = targetCounty; }
    public SubCountyModel getTargetSubCounty() { return targetSubCounty; }
    public void setTargetSubCounty(SubCountyModel targetSubCounty) { this.targetSubCounty = targetSubCounty; }
    public WardModel getTargetWard() { return targetWard; }
    public void setTargetWard(WardModel targetWard) { this.targetWard = targetWard; }
    public MemberGroupModel getTargetGroup() { return targetGroup; }
    public void setTargetGroup(MemberGroupModel targetGroup) { this.targetGroup = targetGroup; }
    public RegistrationType getRegistrationType() { return registrationType; }
    public void setRegistrationType(RegistrationType registrationType) { this.registrationType = registrationType; }
    public TransferRequestStatus getStatus() { return status; }
    public void setStatus(TransferRequestStatus status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public SystemUserModel getRequestedBy() { return requestedBy; }
    public void setRequestedBy(SystemUserModel requestedBy) { this.requestedBy = requestedBy; }
    public SystemUserModel getApprovedBy() { return approvedBy; }
    public void setApprovedBy(SystemUserModel approvedBy) { this.approvedBy = approvedBy; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
}

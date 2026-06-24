package com.SUNData.MemberApp.Model.GroupModel;

import com.SUNData.MemberApp.Model.LocationModel.CountyModel;
import com.SUNData.MemberApp.Model.LocationModel.SubCountyModel;
import com.SUNData.MemberApp.Model.LocationModel.WardModel;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_groups")
public class MemberGroupModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id", unique = true)
    private String groupId;

    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    @Column(name = "date_created", updatable = false)
    private LocalDateTime dateCreated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "county_id", nullable = false)
    private CountyModel county;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_county_id", nullable = false)
    private SubCountyModel subCounty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id", nullable = false)
    private WardModel ward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private SystemUserModel createdBy;

    public MemberGroupModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDateTime dateCreated) { this.dateCreated = dateCreated; }

    public CountyModel getCounty() { return county; }
    public void setCounty(CountyModel county) { this.county = county; }

    public SubCountyModel getSubCounty() { return subCounty; }
    public void setSubCounty(SubCountyModel subCounty) { this.subCounty = subCounty; }

    public WardModel getWard() { return ward; }
    public void setWard(WardModel ward) { this.ward = ward; }

    public SystemUserModel getCreatedBy() { return createdBy; }
    public void setCreatedBy(SystemUserModel createdBy) { this.createdBy = createdBy; }
}

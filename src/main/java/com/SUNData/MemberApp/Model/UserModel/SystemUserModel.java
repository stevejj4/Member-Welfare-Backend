package com.SUNData.MemberApp.Model.UserModel;

import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Model.LocationModel.CountyModel;
import com.SUNData.MemberApp.Model.LocationModel.SubCountyModel;
import com.SUNData.MemberApp.Model.LocationModel.WardModel;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "system_user")
public class SystemUserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String password;
    // stored as BCrypt hash

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "must_change_password", nullable = false)
    private boolean mustChangePassword = false;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_county_id")
    private CountyModel assignedCounty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_sub_county_id")
    private SubCountyModel assignedSubCounty;

    @ManyToMany
    @JoinTable(
            name = "system_user_assigned_wards",
            joinColumns = @JoinColumn(name = "system_user_id"),
            inverseJoinColumns = @JoinColumn(name = "ward_id")
    )
    private Set<WardModel> assignedWards = new HashSet<>();

    // Audit fields
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(updatable = false)
    private LocalDateTime updatedAt;

    public SystemUserModel() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public boolean isMustChangePassword() { return mustChangePassword; }
    public void setMustChangePassword(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public CountyModel getAssignedCounty() { return assignedCounty; }
    public void setAssignedCounty(CountyModel assignedCounty) { this.assignedCounty = assignedCounty; }

    public SubCountyModel getAssignedSubCounty() { return assignedSubCounty; }
    public void setAssignedSubCounty(SubCountyModel assignedSubCounty) { this.assignedSubCounty = assignedSubCounty; }

    public Set<WardModel> getAssignedWards() { return assignedWards; }
    public void setAssignedWards(Set<WardModel> assignedWards) { this.assignedWards = assignedWards; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

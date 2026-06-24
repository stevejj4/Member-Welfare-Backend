package com.SUNData.MemberApp.DTOs.Auth;

import java.util.List;

import lombok.Data;
@Data
public class AuthResponseDTO {
    private String token;
    private String role;
    private Long id;
    private String email;
    private String fullName;
    private List<String> permissions;
    private Long countyId;
    private String countyName;
    private Long subCountyId;
    private String subCountyName;
    private List<Long> wardIds;
    private List<String> wardNames;

    public AuthResponseDTO(String token, String role, Long id, String email, String fullName,
                           List<String> permissions) {
        this(token, role, id, email, fullName, permissions, null, null, null, null, List.of(), List.of());
    }

    public AuthResponseDTO(String token, String role, Long id, String email, String fullName,
                           List<String> permissions, Long countyId, String countyName,
                           Long subCountyId, String subCountyName, List<Long> wardIds,
                           List<String> wardNames) {
        this.token = token;
        this.role = role;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.permissions = permissions;
        this.countyId = countyId;
        this.countyName = countyName;
        this.subCountyId = subCountyId;
        this.subCountyName = subCountyName;
        this.wardIds = wardIds;
        this.wardNames = wardNames;
    }

    public String getToken() { return token; }
    public String getRole() { return role; }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public List<String> getPermissions() { return permissions; }
    public Long getCountyId() { return countyId; }
    public String getCountyName() { return countyName; }
    public Long getSubCountyId() { return subCountyId; }
    public String getSubCountyName() { return subCountyName; }
    public List<Long> getWardIds() { return wardIds; }
    public List<String> getWardNames() { return wardNames; }
}

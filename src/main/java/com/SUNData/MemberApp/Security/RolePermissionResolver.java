package com.SUNData.MemberApp.Security;

import com.SUNData.MemberApp.Enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Maps legacy {@link UserRole} values to fine-grained permission authorities
 * until permissions are stored per-user in the database.
 */
public final class RolePermissionResolver {

    private RolePermissionResolver() {
    }

    public static List<String> permissionsFor(UserRole role) {
        if (role == null) {
            return List.of();
        }

        return switch (role) {
            case ADMIN -> List.of(
                    Permission.MEMBER_CREATE,
                    Permission.MEMBER_READ,
                    Permission.MEMBER_WRITE
            );
            case COORDINATOR -> List.of(
                    Permission.MEMBER_CREATE,
                    Permission.MEMBER_READ,
                    Permission.MEMBER_WRITE
            );
            case FACILITATOR -> List.of(
                    Permission.MEMBER_CREATE,
                    Permission.MEMBER_READ,
                    Permission.MEMBER_WRITE
            );
        };
    }

    public static Collection<GrantedAuthority> authoritiesFor(UserRole role) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
            for (String permission : permissionsFor(role)) {
                authorities.add(new SimpleGrantedAuthority(permission));
            }
        }

        return authorities;
    }

    public static Collection<GrantedAuthority> authoritiesForRoleName(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            return List.of();
        }

        String normalized = roleName.startsWith("ROLE_")
                ? roleName.substring(5)
                : roleName;

        try {
            return authoritiesFor(UserRole.valueOf(normalized.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + normalized.toUpperCase()));
        }
    }
}

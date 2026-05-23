package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Navigation.NavigationItemDTO;
import com.SUNData.MemberApp.Security.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

    @GetMapping("/navigation")
    public List<NavigationItemDTO> getNavigation(Authentication authentication) {
        List<NavigationItemDTO> items = new ArrayList<>();

        if (authentication == null) {
            return items;
        }

        if (hasAuthority(authentication, "ROLE_ADMIN")) {
            items.add(new NavigationItemDTO("Admin Dashboard", "layout-dashboard", "/admin"));
            items.add(new NavigationItemDTO("Users", "users", "/admin/users"));
        }

        boolean isAdmin = hasAuthority(authentication, "ROLE_ADMIN");

        if (hasAuthority(authentication, Permission.MEMBER_READ)) {
            if (!isAdmin) {
                items.add(new NavigationItemDTO("Dashboard", "layout-dashboard", "/dashboard"));
            }
            items.add(new NavigationItemDTO("Members", "clipboard-list", "/members"));
        }

        if (hasAuthority(authentication, Permission.MEMBER_CREATE)) {
            items.add(new NavigationItemDTO("Registration", "user-plus", "/register"));
        }

        return items;
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority::equals);
    }
}

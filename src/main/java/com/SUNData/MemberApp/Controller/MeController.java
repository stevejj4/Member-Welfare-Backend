package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Location.WardDTO;
import com.SUNData.MemberApp.DTOs.Navigation.NavigationItemDTO;
import com.SUNData.MemberApp.DTOs.User.AssignmentDTO;
import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Exceptions.ResourceNotFoundException;
import com.SUNData.MemberApp.Exceptions.ValidationException;
import com.SUNData.MemberApp.Model.LocationModel.WardModel;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.SystemUserRepository;
import com.SUNData.MemberApp.Repository.WardRepository;
import com.SUNData.MemberApp.Security.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

    private final SystemUserRepository userRepository;
    private final WardRepository wardRepository;

    public MeController(SystemUserRepository userRepository, WardRepository wardRepository) {
        this.userRepository = userRepository;
        this.wardRepository = wardRepository;
    }

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

        if (hasAnyAuthority(authentication, Permission.MEMBER_READ, "ROLE_ADMIN", "ROLE_COORDINATOR", "ROLE_FACILITATOR")) {
            if (!isAdmin) {
                items.add(new NavigationItemDTO("Dashboard", "layout-dashboard", "/dashboard"));
            }
            items.add(new NavigationItemDTO("Members", "clipboard-list", "/members"));
        }

        if (hasAnyAuthority(authentication, Permission.MEMBER_CREATE, "ROLE_ADMIN", "ROLE_COORDINATOR", "ROLE_FACILITATOR")) {
            items.add(new NavigationItemDTO("Registration", "user-plus", "/register"));
        }

        if (hasAnyAuthority(authentication, Permission.GROUP_READ, "ROLE_ADMIN", "ROLE_COORDINATOR", "ROLE_FACILITATOR")) {
            items.add(new NavigationItemDTO("Groups", "users-round", "/groups"));
        }

        return items;
    }

    @GetMapping("/assignment")
    public AssignmentDTO getAssignment(Authentication authentication) {
        return new AssignmentDTO(currentUser(authentication));
    }

    @GetMapping("/wards")
    public List<WardDTO> getMyWards(Authentication authentication) {
        SystemUserModel user = currentUser(authentication);

        if (user.getRole() == UserRole.ADMIN) {
            return wardRepository.findAll().stream()
                    .sorted(Comparator.comparing(WardModel::getName))
                    .map(WardDTO::new)
                    .toList();
        }

        if (user.getRole() == UserRole.COORDINATOR) {
            if (user.getAssignedSubCounty() == null) {
                throw new ValidationException("Your account has no assigned sub-county");
            }
            return wardRepository.findBySubCountyId(user.getAssignedSubCounty().getId()).stream()
                    .sorted(Comparator.comparing(WardModel::getName))
                    .map(WardDTO::new)
                    .toList();
        }

        if (user.getAssignedWards() == null || user.getAssignedWards().isEmpty()) {
            throw new ValidationException("Your account has no assigned wards");
        }
        return user.getAssignedWards().stream()
                .sorted(Comparator.comparing(WardModel::getName))
                .map(WardDTO::new)
                .toList();
    }

    private SystemUserModel currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ValidationException("Authenticated user is required");
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority::equals);
    }

    private boolean hasAnyAuthority(Authentication authentication, String... authorities) {
        for (String authority : authorities) {
            if (hasAuthority(authentication, authority)) {
                return true;
            }
        }
        return false;
    }
}

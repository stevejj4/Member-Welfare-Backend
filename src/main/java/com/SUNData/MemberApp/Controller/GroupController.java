package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Group.CreateGroupRequestDTO;
import com.SUNData.MemberApp.DTOs.Group.GroupDetailsDTO;
import com.SUNData.MemberApp.DTOs.Group.MemberGroupDTO;
import com.SUNData.MemberApp.Service.group.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('GROUP_CREATE')")
    public ResponseEntity<MemberGroupDTO> createGroup(@Valid @RequestBody CreateGroupRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createGroup(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GROUP_READ')")
    public ResponseEntity<List<MemberGroupDTO>> getGroups(@RequestParam(required = false) Long wardId) {
        return ResponseEntity.ok(groupService.getGroups(wardId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GROUP_READ')")
    public ResponseEntity<GroupDetailsDTO> getGroupDetails(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupDetails(id));
    }
}

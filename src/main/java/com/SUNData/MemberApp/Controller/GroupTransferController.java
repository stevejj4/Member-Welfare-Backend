package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Group.CreateGroupTransferRequestDTO;
import com.SUNData.MemberApp.DTOs.Group.GroupTransferRequestDTO;
import com.SUNData.MemberApp.DTOs.Group.MemberGroupDTO;
import com.SUNData.MemberApp.Service.group.GroupTransferRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group-transfers")
public class GroupTransferController {
    private static final String TRANSFER_ACCESS = "isAuthenticated()";

    private final GroupTransferRequestService transferService;

    public GroupTransferController(GroupTransferRequestService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/groups/{groupId}")
    @PreAuthorize(TRANSFER_ACCESS)
    public ResponseEntity<GroupTransferRequestDTO> createTransferRequest(
            @PathVariable Long groupId,
            @Valid @RequestBody CreateGroupTransferRequestDTO request) {
        return ResponseEntity.ok(transferService.createRequest(groupId, request));
    }

    @GetMapping("/history")
    @PreAuthorize(TRANSFER_ACCESS)
    public ResponseEntity<List<GroupTransferRequestDTO>> getHistory() {
        return ResponseEntity.ok(transferService.getHistory());
    }

    @PatchMapping("/{requestId}/approve")
    @PreAuthorize(TRANSFER_ACCESS)
    public ResponseEntity<MemberGroupDTO> approveTransfer(@PathVariable Long requestId) {
        return ResponseEntity.ok(transferService.approve(requestId));
    }
}

package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Member.CreateMemberTransferRequestDTO;
import com.SUNData.MemberApp.DTOs.Member.ApproveMemberTransferRequestDTO;
import com.SUNData.MemberApp.DTOs.Member.MemberDetailsDTO;
import com.SUNData.MemberApp.DTOs.Member.MemberTransferRequestDTO;
import com.SUNData.MemberApp.Service.member.MemberTransferRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/member-transfers")
public class MemberTransferController {
    private static final String TRANSFER_ACCESS = "isAuthenticated()";

    private final MemberTransferRequestService transferService;

    public MemberTransferController(MemberTransferRequestService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/members/{memberId}")
    @PreAuthorize(TRANSFER_ACCESS)
    public ResponseEntity<MemberTransferRequestDTO> createTransferRequest(
            @PathVariable Long memberId,
            @Valid @RequestBody CreateMemberTransferRequestDTO request) {
        return ResponseEntity.ok(transferService.createRequest(memberId, request));
    }

    @GetMapping("/history")
    @PreAuthorize(TRANSFER_ACCESS)
    public ResponseEntity<List<MemberTransferRequestDTO>> getTransferHistory() {
        return ResponseEntity.ok(transferService.getHistory());
    }

    @PatchMapping("/{requestId}/approve")
    @PreAuthorize(TRANSFER_ACCESS)
    public ResponseEntity<MemberDetailsDTO> approveTransfer(
            @PathVariable Long requestId,
            @Valid @RequestBody ApproveMemberTransferRequestDTO request) {
        return ResponseEntity.ok(transferService.approve(requestId, request));
    }
}

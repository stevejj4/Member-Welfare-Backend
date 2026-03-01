package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.Model.NextOfKinModel;
import com.SUNData.MemberApp.Service.NextOfKinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members/{principalId}/nextofkin") // Base path includes the Principal ID
public class NextOfKinController {

    private final NextOfKinService nextOfKinService;

    public NextOfKinController(NextOfKinService nextOfKinService) {
        this.nextOfKinService = nextOfKinService;
    }

    /**
     * ✅ Recommended: Add Kin only if none exists.
     * This enforces your "Search First" rule because the ID is in the URL.
     */
    @PostMapping
    public ResponseEntity<NextOfKinModel> addNextOfKin(@PathVariable Long principalId,
                                                       @RequestBody NextOfKinModel nextOfKin) {
        // Uses the facilitator method from your service to ensure only 1 kin per member
        NextOfKinModel saved = nextOfKinService.addNextOfKin(principalId, nextOfKin);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NextOfKinModel> updateNextOfKin(@PathVariable Long principalId,
                                                          @PathVariable Long id,
                                                          @RequestBody NextOfKinModel updated) {
        // We use the ID to update the specific record
        NextOfKinModel saved = nextOfKinService.updateNextOfKin(id, updated);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextOfKin(@PathVariable Long principalId,
                                                @PathVariable Long id) {
        nextOfKinService.deleteNextOfKin(id);
        return ResponseEntity.noContent().build();
    }
}

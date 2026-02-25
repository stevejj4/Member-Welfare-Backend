package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.Model.NextOfKinModel;
import com.SUNData.MemberApp.Service.NextOfKinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nextofkin")
public class NextOfKinController {

    private final NextOfKinService nextOfKinService;

    public NextOfKinController(NextOfKinService nextOfKinService) {
        this.nextOfKinService = nextOfKinService;
    }

    @GetMapping
    public List<NextOfKinModel> getAllNextOfKin() {
        return nextOfKinService.getAllNextOfKin();
    }

    @GetMapping("/principal/{principalId}")
    public List<NextOfKinModel> getNextOfKinByPrincipal(@PathVariable Long principalId) {
        return nextOfKinService.getNextOfKinByPrincipalMember(principalId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NextOfKinModel> getNextOfKinById(@PathVariable Long id) {
        return nextOfKinService.getNextOfKinById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NextOfKinModel> addNextOfKin(@RequestBody NextOfKinModel nextOfKin) {
        NextOfKinModel saved = nextOfKinService.addNextOfKin(nextOfKin);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ✅ Facilitator endpoint: only add if none exists
    @PostMapping("/facilitator/add/{principalId}")
    public ResponseEntity<NextOfKinModel> addNextOfKinIfNone(@PathVariable Long principalId,
                                                             @RequestBody NextOfKinModel nextOfKin) {
        NextOfKinModel saved = nextOfKinService.addNextOfKinIfNone(principalId, nextOfKin);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NextOfKinModel> updateNextOfKin(@PathVariable Long id,
                                                          @RequestBody NextOfKinModel updated) {
        NextOfKinModel saved = nextOfKinService.updateNextOfKin(id, updated);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextOfKin(@PathVariable Long id) {
        nextOfKinService.deleteNextOfKin(id);
        return ResponseEntity.noContent().build();
    }
}
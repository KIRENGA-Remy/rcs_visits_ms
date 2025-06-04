package gitoli.java.projects.com.rcs_visits_ms.role_visitor.controller;

import gitoli.java.projects.com.rcs_visits_ms.role_visitor.dto.VisitorRequest;
import gitoli.java.projects.com.rcs_visits_ms.role_visitor.dto.VisitorResponse;
import gitoli.java.projects.com.rcs_visits_ms.role_visitor.service.VisitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    @PostMapping
    public ResponseEntity<VisitorResponse> createVisitor(@Valid @RequestBody VisitorRequest request) {
        VisitorResponse response = visitorService.createVisitor(request);
        return new ResponseEntity<>(response, response.getMessage().contains("successfully")
                ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitorResponse> getVisitor(@PathVariable UUID id) {
        VisitorResponse response = visitorService.getVisitorById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prisoner/{prisonerId}")
    public ResponseEntity<List<VisitorResponse>> getVisitorsByPrisonerId(@PathVariable UUID prisonerId) {
        List<VisitorResponse> responses = visitorService.getAllVisitorsByPrisonerId(prisonerId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitorResponse> updateVisitor(@PathVariable UUID id,
                                                         @Valid @RequestBody VisitorRequest request) {
        VisitorResponse response = visitorService.updateVisitor(id, request);
        return new ResponseEntity<>(response, response.getMessage().contains("successfully")
                ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisitor(@PathVariable UUID id) {
        visitorService.deleteVisitor(id);
        return ResponseEntity.noContent().build();
    }
}
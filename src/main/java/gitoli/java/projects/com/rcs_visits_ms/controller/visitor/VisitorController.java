package gitoli.java.projects.com.rcs_visits_ms.controller.visitor;

import com.gitoli.rcs_visits_ms.dto.VisitorRequest;
import com.gitoli.rcs_visits_ms.dto.VisitorResponse;
import com.gitoli.rcs_visits_ms.service.VisitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
public class VisitorController {

    private static final Logger logger = LoggerFactory.getLogger(VisitorController.class);

    private final VisitorService visitorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('VISITOR')")
    public ResponseEntity<VisitorResponse> createVisitor(@Valid @RequestBody VisitorRequest request) {
        logger.info("Received request to create visitor with email: {}", request.getEmail());
        VisitorResponse response = visitorService.createVisitor(request);
        return new ResponseEntity<>(response, response.getMessage().contains("successfully")
                ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VISITOR')")
    public ResponseEntity<VisitorResponse> getVisitor(@PathVariable UUID id) {
        logger.info("Received request to get visitor with ID: {}", id);
        VisitorResponse response = visitorService.getVisitorById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prisoner/{prisonerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LAWYER')")
    public ResponseEntity<List<VisitorResponse>> getVisitorsByPrisonerId(@PathVariable UUID prisonerId) {
        logger.info("Received request to get visitors for prisoner ID: {}", prisonerId);
        List<VisitorResponse> responses = visitorService.getAllVisitorsByPrisonerId(prisonerId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VISITOR')")
    public ResponseEntity<VisitorResponse> updateVisitor(@PathVariable UUID id,
                                                         @Valid @RequestBody VisitorRequest request) {
        logger.info("Received request to update visitor with ID: {}", id);
        VisitorResponse response = visitorService.updateVisitor(id, request);
        return new ResponseEntity<>(response, response.getMessage().contains("successfully")
                ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VISITOR')")
    public ResponseEntity<Void> deleteVisitor(@PathVariable UUID id) {
        logger.info("Received request to delete visitor with ID: {}", id);
        visitorService.deleteVisitor(id);
        return ResponseEntity.noContent().build();
    }
}

import gitoli.java.projects.com.rcs_visits_ms.dto.CreateVisitorDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.LoginDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.prisoner.PrisonerDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.VisitRequestDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.VisitScheduleDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.visitor.VisitorDTO;
import gitoli.java.projects.com.rcs_visits_ms.service.visitor.VisitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
@Tag(name = "Visitor Management", description = "APIs for managing visitors")
@SecurityRequirement(name = "bearerAuth")
public class VisitorController {

    private final VisitorService visitorService;

    @Operation(summary = "Register a new visitor", description = "Registers a new visitor with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Visitor registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate email/national ID/phone number")
    })
    @PostMapping("/register")
    public ResponseEntity<VisitorDTO> registerVisitor(@Valid @RequestBody CreateVisitorDTO dto) {
        VisitorDTO visitor = visitorService.registerVisitor(dto);
        return new ResponseEntity<>(visitor, HttpStatus.CREATED);
    }

    @Operation(summary = "Visitor login", description = "Authenticates a visitor using email and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/login")
    public ResponseEntity<VisitorDTO> loginVisitor(@Valid @RequestBody LoginDTO dto) {
        VisitorDTO visitor = visitorService.loginVisitor(dto);
        return ResponseEntity.ok(visitor);
    }

    @Operation(summary = "Get visitor details", description = "Retrieves details of the authenticated visitor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visitor details retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Visitor role required"),
            @ApiResponse(responseCode = "404", description = "Visitor not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('VISITOR') and #id == principal.id")
    public ResponseEntity<VisitorDTO> getVisitorDetails(@PathVariable UUID id) {
        VisitorDTO visitor = visitorService.getVisitorDetails(id);
        return ResponseEntity.ok(visitor);
    }

    @Operation(summary = "Download visit slip", description = "Generates and downloads a PDF slip for the visitor's visit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visit slip generated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Visitor role required"),
            @ApiResponse(responseCode = "404", description = "Visitor not found")
    })
    @GetMapping("/{visitorId}/slip")
    @PreAuthorize("hasRole('VISITOR') and #visitorId == principal.id")
    public ResponseEntity<byte[]> downloadVisitSlip(@PathVariable UUID visitorId) {
        byte[] slip = visitorService.generateVisitSlip(visitorId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "visit-slip.pdf");
        return new ResponseEntity<>(slip, headers, HttpStatus.OK);
    }

    @Operation(summary = "Get visit schedule", description = "Retrieves the visit schedule for the authenticated visitor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visit schedule retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Visitor role required"),
            @ApiResponse(responseCode = "404", description = "Visitor not found")
    })
    @GetMapping("/{visitorId}/schedule")
    @PreAuthorize("hasRole('VISITOR') and #visitorId == principal.id")
    public ResponseEntity<VisitScheduleDTO> getVisitSchedule(@PathVariable UUID visitorId) {
        VisitScheduleDTO schedule = visitorService.getVisitSchedule(visitorId);
        return ResponseEntity.ok(schedule);
    }

    @Operation(summary = "Get prisoner details", description = "Retrieves details of the prisoner associated with the visitor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prisoner details retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Visitor role required"),
            @ApiResponse(responseCode = "404", description = "Visitor or prisoner not found")
    })
    @GetMapping("/{visitorId}/prisoner")
    @PreAuthorize("hasRole('VISITOR') and #visitorId == principal.id")
    public ResponseEntity<PrisonerDTO> getPrisonerDetails(@PathVariable UUID visitorId) {
        PrisonerDTO prisoner = visitorService.getPrisonerDetails(visitorId);
        return ResponseEntity.ok(prisoner);
    }

    @Operation(summary = "Request a visit", description = "Submits a visit request for approval")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visit request submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Visitor role required"),
            @ApiResponse(responseCode = "404", description = "Visitor or prisoner not found")
    })
    @PostMapping("/request")
    @PreAuthorize("hasRole('VISITOR')")
    public ResponseEntity<VisitorDTO> requestVisit(@Valid @RequestBody VisitRequestDTO dto) {
        VisitorDTO visitor = visitorService.requestVisit(dto);
        return ResponseEntity.ok(visitor);
    }

    @Operation(summary = "Approve visit request", description = "Approves a visitor's visit request (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visit request approved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required"),
            @ApiResponse(responseCode = "404", description = "Visitor not found")
    })
    @PutMapping("/{visitorId}/approve")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<VisitorDTO> approveVisitRequest(@PathVariable UUID visitorId) {
        VisitorDTO visitor = visitorService.approveVisitRequest(visitorId);
        return ResponseEntity.ok(visitor);
    }

    @Operation(summary = "Reject visit request", description = "Rejects a visitor's visit request (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visit request rejected successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required"),
            @ApiResponse(responseCode = "404", description = "Visitor not found")
    })
    @PutMapping("/{visitorId}/reject")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<VisitorDTO> rejectVisitRequest(@PathVariable UUID visitorId) {
        VisitorDTO visitor = visitorService.rejectVisitRequest(visitorId);
        return ResponseEntity.ok(visitor);
    }

    @Operation(summary = "Get all visitors", description = "Retrieves all active visitors (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visitors retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Page<VisitorDTO>> getVisitors(Pageable pageable) {
        Page<VisitorDTO> visitors = visitorService.getVisitors(pageable);
        return ResponseEntity.ok(visitors);
    }

    @Operation(summary = "Search visitors by prisoner ID", description = "Retrieves visitors associated with a prisoner (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visitors retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required"),
            @ApiResponse(responseCode = "404", description = "Prisoner not found")
    })
    @GetMapping("/search/prisoner/{prisonerId}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Page<VisitorDTO>> searchVisitorsByPrisonerId(@PathVariable UUID prisonerId, Pageable pageable) {
        Page<VisitorDTO> visitors = visitorService.searchVisitorsByPrisonerId(prisonerId, pageable);
        return ResponseEntity.ok(visitors);
    }
}
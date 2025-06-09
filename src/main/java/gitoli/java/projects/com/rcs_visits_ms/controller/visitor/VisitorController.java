package gitoli.java.projects.com.rcs_visits_ms.controller;

import gitoli.java.projects.com.rcs_visits_ms.dto.CreateVisitorDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.SearchVisitorDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.VisitorDTO;
import gitoli.java.projects.com.rcs_visits_ms.service.VisitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Create a new visitor", description = "Creates a new visitor (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Visitor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate email/national ID/phone number"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VisitorDTO> createVisitor(@Valid @RequestBody CreateVisitorDTO dto) {
        VisitorDTO visitor = visitorService.createVisitor(dto);
        return new ResponseEntity<>(visitor, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a visitor", description = "Updates an existing visitor by ID (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visitor updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate email/national ID/phone number"),
            @ApiResponse(responseCode = "404", description = "Visitor not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VisitorDTO> updateVisitor(@PathVariable UUID id, @Valid @RequestBody CreateVisitorDTO dto) {
        VisitorDTO visitor = visitorService.updateVisitor(id, dto);
        return ResponseEntity.ok(visitor);
    }

    @Operation(summary = "Get a visitor by ID", description = "Retrieves a visitor by ID (Admin or the visitor themselves)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visitor retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Visitor not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin or visitor role required")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('VISITOR') and #id == principal.id)")
    public ResponseEntity<VisitorDTO> getVisitor(@PathVariable UUID id) {
        VisitorDTO visitor = visitorService.getVisitor(id);
        return ResponseEntity.ok(visitor);
    }

    @Operation(summary = "Get a visitor by email", description = "Retrieves a visitor by email (Visitor only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visitor retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Visitor not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Visitor role required")
    })
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('VISITOR')")
    public ResponseEntity<VisitorDTO> getVisitorByEmail(@PathVariable String email) {
        VisitorDTO visitor = visitorService.getVisitorByEmail(email);
        return ResponseEntity.ok(visitor);
    }

    @Operation(summary = "Delete a visitor", description = "Deletes a visitor by ID (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Visitor deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Visitor not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVisitor(@PathVariable UUID id) {
        visitorService.deleteVisitor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search visitors", description = "Searches visitors by various criteria (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visitors retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<VisitorDTO>> searchVisitors(@ModelAttribute SearchVisitorDTO dto, Pageable pageable) {
        Page<VisitorDTO> visitors = visitorService.searchVisitors(dto, pageable);
        return ResponseEntity.ok(visitors);
    }
}
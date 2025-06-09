package gitoli.java.projects.com.rcs_visits_ms.controller.lawyer;

import gitoli.java.projects.com.rcs_visits_ms.dto.lawyer.CreateLawyerDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.lawyer.LawyerDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.lawyer.SearchLawyerDTO;
import gitoli.java.projects.com.rcs_visits_ms.service.lawyer.LawyerService;
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
@RequestMapping("/api/lawyers")
@RequiredArgsConstructor
@Tag(name = "Lawyer Management", description = "APIs for managing lawyers")
@SecurityRequirement(name = "bearerAuth")
public class LawyerController {
    private final LawyerService lawyerService;

    @Operation(summary = "Create a new lawyer", description = "Creates a new lawyer (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lawyer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate email/national ID/phone number/company name"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LawyerDTO> createLawyer(@Valid @RequestBody CreateLawyerDTO dto) {
        LawyerDTO lawyer = lawyerService.createLawyer(dto);
        return new ResponseEntity<>(lawyer, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a lawyer", description = "Updates an existing lawyer by ID (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lawyer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate email/national ID/phone number/company name"),
            @ApiResponse(responseCode = "404", description = "Lawyer not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LawyerDTO> updateLawyer(@PathVariable UUID id, @Valid @RequestBody CreateLawyerDTO dto) {
        LawyerDTO lawyer = lawyerService.updateLawyer(id, dto);
        return ResponseEntity.ok(lawyer);
    }

    @Operation(summary = "Get a lawyer by ID", description = "Retrieves a lawyer by ID (Admin or the lawyer themselves)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lawyer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Lawyer not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin or lawyer role required")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('LAWYER') and #id == principal.id)")
    public ResponseEntity<LawyerDTO> getLawyer(@PathVariable UUID id) {
        LawyerDTO lawyer = lawyerService.getLawyer(id);
        return ResponseEntity.ok(lawyer);
    }

    @Operation(summary = "Get a lawyer by email", description = "Retrieves a lawyer by email (Lawyer only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lawyer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Lawyer not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Lawyer role required")
    })
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('LAWYER')")
    public ResponseEntity<LawyerDTO> getLawyerByEmail(@PathVariable String email) {
        LawyerDTO lawyer = lawyerService.getLawyerByEmail(email);
        return ResponseEntity.ok(lawyer);
    }

    @Operation(summary = "Delete a lawyer", description = "Deletes a lawyer by ID (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Lawyer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Lawyer not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLawyer(@PathVariable UUID id) {
        lawyerService.deleteLawyer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search lawyers", description = "Searches lawyers by various criteria (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lawyers retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<LawyerDTO>> searchLawyers(@ModelAttribute SearchLawyerDTO dto, Pageable pageable) {
        Page<LawyerDTO> lawyers = lawyerService.searchLawyers(dto, pageable);
        return ResponseEntity.ok(lawyers);
    }
}
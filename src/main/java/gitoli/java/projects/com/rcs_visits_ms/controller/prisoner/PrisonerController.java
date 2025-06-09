package gitoli.java.projects.com.rcs_visits_ms.controller.prisoner;

import gitoli.java.projects.com.rcs_visits_ms.dto.prisoner.CreatePrisonerDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.prisoner.PrisonerDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.prisoner.SearchPrisonerDTO;
import gitoli.java.projects.com.rcs_visits_ms.service.prisoner.PrisonerService;
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
@RequestMapping("/api/prisoners")
@RequiredArgsConstructor
@Tag(name = "Prisoner Management", description = "APIs for managing prisoners (Admin only)")
@SecurityRequirement(name = "bearerAuth")
public class PrisonerController {
    private final PrisonerService prisonerService;

    @Operation(summary = "Create a new prisoner", description = "Creates a new prisoner with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prisoner created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate national ID/email"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrisonerDTO> createPrisoner(@Valid @RequestBody CreatePrisonerDTO dto) {
        PrisonerDTO prisoner = prisonerService.createPrisoner(dto);
        return new ResponseEntity<>(prisoner, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a prisoner", description = "Updates an existing prisoner by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prisoner updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate national ID/email"),
            @ApiResponse(responseCode = "404", description =
                    "Prisoner not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrisonerDTO> updatePrisoner(@PathVariable UUID id, @Valid @RequestBody CreatePrisonerDTO dto) {
        PrisonerDTO prisoner = prisonerService.updatePrisoner(id, dto);
        return ResponseEntity.ok(prisoner);
    }

    @Operation(summary = "Get a prisoner by ID", description = "Retrieves a prisoner by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prisoner retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Prisoner not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrisonerDTO> getPrisoner(@PathVariable UUID id) {
        PrisonerDTO prisoner = prisonerService.getPrisoner(id);
        return ResponseEntity.ok(prisoner);
    }

    @Operation(summary = "Delete a prisoner", description = "Deletes a prisoner by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Prisoner deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Prisoner not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePrisoner(@PathVariable UUID id) {
        prisonerService.deletePrisoner(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search prisoners", description = "Searches prisoners by status, court status, nationality, or name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prisoners retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PrisonerDTO>> searchPrisoners(@ModelAttribute SearchPrisonerDTO dto, Pageable pageable) {
        Page<PrisonerDTO> prisoners = prisonerService.searchPrisoners(dto, pageable);
        return ResponseEntity.ok(prisoners);
    }
}
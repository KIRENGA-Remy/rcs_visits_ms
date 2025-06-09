package gitoli.java.projects.com.rcs_visits_ms.controller.admin;

import gitoli.java.projects.com.rcs_visits_ms.dto.admin.CreateAdminDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.admin.SearchAdminDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.admin.AdminDTO;
import gitoli.java.projects.com.rcs_visits_ms.service.admin.AdminService;
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
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "APIs for managing admins")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "Create a new admin", description = "Creates a new admin (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Admin created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate email/phone number/office name"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDTO> createAdmin(@Valid @RequestBody CreateAdminDTO dto) {
        AdminDTO admin = adminService.createAdmin(dto);
        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an admin", description = "Updates an existing admin by ID (Admin only, own profile)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate email/phone number/office name"),
            @ApiResponse(responseCode = "404", description = "Admin not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required or not own profile")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and #id == principal.id")
    public ResponseEntity<AdminDTO> updateAdmin(@PathVariable UUID id, @Valid @RequestBody CreateAdminDTO dto) {
        AdminDTO admin = adminService.updateAdmin(id, dto);
        return ResponseEntity.ok(admin);
    }

    @Operation(summary = "Get an admin by ID", description = "Retrieves an admin by ID (Admin only, own profile)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Admin not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required or not own profile")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and #id == principal.id")
    public ResponseEntity<AdminDTO> getAdmin(@PathVariable UUID id) {
        AdminDTO admin = adminService.getAdmin(id);
        return ResponseEntity.ok(admin);
    }

    @Operation(summary = "Get an admin by email", description = "Retrieves an admin by email (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Admin not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDTO> getAdminByEmail(@PathVariable String email) {
        AdminDTO admin = adminService.getAdminByEmail(email);
        return ResponseEntity.ok(admin);
    }

    @Operation(summary = "Delete an admin", description = "Deletes an admin by ID (Admin only, own profile)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Admin deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Admin not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required or not own profile")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and #id == principal.id")
    public ResponseEntity<Void> deleteAdmin(@PathVariable UUID id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search admins", description = "Searches admins by various criteria (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admins retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminDTO>> searchAdmins(@ModelAttribute SearchAdminDTO dto, Pageable pageable) {
        Page<AdminDTO> admins = adminService.searchAdmins(dto, pageable);
        return ResponseEntity.ok(admins);
    }
}
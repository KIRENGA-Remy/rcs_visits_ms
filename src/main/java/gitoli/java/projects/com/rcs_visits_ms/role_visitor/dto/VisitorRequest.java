package gitoli.java.projects.com.rcs_visits_ms.role_visitor.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitorRequest {
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Email(message = "Invalid email format")
    @Size(max = 70, message = "Email must not exceed 70 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 70, message = "Password must be between 8 and 70 characters")
    private String password;

    @NotBlank(message = "Relationship is required")
    @Size(max = 100, message = "Relationship must not exceed 100 characters")
    private String relationship;

    @NotNull(message = "Number of visitors is required")
    @Min(value = 1, message = "Number of visitors must be at least 1")
    private Integer numberOfVisitors;

    @NotNull(message = "Prisoner ID is required")
    private UUID prisonerId;

    @NotBlank(message = "Phone number is required")
    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    private String phoneNumber;

    @NotBlank(message = "National ID is required")
    @Size(max = 70, message = "National ID must not exceed 70 characters")
    private String nationalId;
}
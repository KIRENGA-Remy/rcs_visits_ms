package gitoli.java.projects.com.rcs_visits_ms.dto;

import gitoli.java.projects.com.rcs_visits_ms.enums.Role;
import gitoli.java.projects.com.rcs_visits_ms.enums.VisitStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVisitorDTO {
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 70, message = "Email must be at most 70 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 70, message = "Password must be between 8 and 70 characters")
    private String password;

    @NotBlank(message = "Relationship is required")
    @Size(max = 100, message = "Relationship must be at most 100 characters")
    private String relationship;

    @NotNull(message = "Number of accompanying visitors is required")
    @Min(value = 0, message = "Number of accompanying visitors cannot be negative")
    private Integer numberOfAccompanyingVisitors;

    @NotNull(message = "Prisoner ID is required")
    private UUID prisonerId;

    @NotBlank(message = "Phone number is required")
    @Size(max = 15, message = "Phone number must be at most 15 characters")
    private String phoneNumber;

    @NotBlank(message = "National ID is required")
    @Size(max = 70, message = "National ID must be at most 70 characters")
    private String nationalId;

    private VisitScheduleDTO visitScheduleDTO;

    @NotNull(message = "Role is required")
    private Role role = Role.VISITOR;

    private VisitStatus visitStatus;
}
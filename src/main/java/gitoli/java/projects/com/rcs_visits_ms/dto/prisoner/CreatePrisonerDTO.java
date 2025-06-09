package gitoli.java.projects.com.rcs_visits_ms.dto.prisoner;

import gitoli.java.projects.com.rcs_visits_ms.enums.CourtStatus;
import gitoli.java.projects.com.rcs_visits_ms.enums.Gender;
import gitoli.java.projects.com.rcs_visits_ms.enums.PrisonerStatus;
import gitoli.java.projects.com.rcs_visits_ms.enums.Role;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePrisonerDTO {
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 70, message = "Email must be at most 70 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 70, message = "Password must be between 8 and 70 characters")
    private String password;

    private Set<UUID> lawyerIds;
    private Set<UUID> visitorIds;

    private LocalDate dateOfImprisonment;
    private LocalDate dateOfRelease;

    @NotNull(message = "Status is required")
    private PrisonerStatus status;

    private CourtStatus courtStatus;

    private String courtReport;

    @NotBlank(message = "Conviction of crime is required")
    private String convictionOfCrime;

    @NotBlank(message = "National ID is required")
    @Size(max = 70, message = "National ID must be at most 70 characters")
    private String nationalId;

    @NotBlank(message = "Nationality is required")
    @Size(max = 50, message = "Nationality must be at most 50 characters")
    private String nationality;

    private String healthDetails;

    @NotNull(message = "Role is required")
    private Role role;
}
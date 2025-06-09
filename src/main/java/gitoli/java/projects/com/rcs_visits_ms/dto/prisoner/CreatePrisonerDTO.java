package gitoli.java.projects.com.rcs_visits_ms.dto.prisoner;

import gitoli.java.projects.com.rcs_visits_ms.enums.CourtStatus;
import gitoli.java.projects.com.rcs_visits_ms.enums.Gender;
import gitoli.java.projects.com.rcs_visits_ms.enums.PrisonerStatus;
import gitoli.java.projects.com.rcs_visits_ms.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePrisonerDTO {
    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotNull
    private Gender gender;

    @NotNull
    @Past
    private LocalDate dateOfBirth;

    @NotBlank
    @Email
    @Size(max = 70)
    private String email;

    @NotBlank
    @Size(min = 8, max = 70)
    private String password;

    private Set<UUID> lawyerIds;
    private Set<UUID> visitorIds;

    private LocalDate dateOfImprisonment;
    private LocalDate dateOfRelease;

    @NotNull
    private PrisonerStatus status;

    private CourtStatus courtStatus;

    private String courtReport;

    @NotBlank
    private String convictionOfCrime;

    @NotBlank
    @Size(max = 70)
    private String nationalId;

    @NotBlank
    @Size(max = 50)
    private String nationality;

    private String healthDetails;

    @NotNull
    private Role role;
}
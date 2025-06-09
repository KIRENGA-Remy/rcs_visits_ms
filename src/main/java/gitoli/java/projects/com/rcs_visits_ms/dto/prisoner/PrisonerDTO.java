package gitoli.java.projects.com.rcs_visits_ms.dto.prisoner;

import gitoli.java.projects.com.rcs_visits_ms.enums.CourtStatus;
import gitoli.java.projects.com.rcs_visits_ms.enums.Gender;
import gitoli.java.projects.com.rcs_visits_ms.enums.PrisonerStatus;
import gitoli.java.projects.com.rcs_visits_ms.enums.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrisonerDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Set<UUID> lawyerIds;
    private Set<UUID> visitorIds;
    private LocalDate dateOfImprisonment;
    private LocalDate dateOfRelease;
    private PrisonerStatus status;
    private CourtStatus courtStatus;
    private String courtReport;
    private String convictionOfCrime;
    private String nationalId;
    private String nationality;
    private String healthDetails;
    private Role role;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private boolean isActive;
}
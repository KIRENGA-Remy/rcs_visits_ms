package gitoli.java.projects.com.rcs_visits_ms.dto.lawyer;

import gitoli.java.projects.com.rcs_visits_ms.entity.VisitSchedule;
import gitoli.java.projects.com.rcs_visits_ms.enums.LawyerDegree;
import gitoli.java.projects.com.rcs_visits_ms.enums.Role;
import gitoli.java.projects.com.rcs_visits_ms.enums.VisitStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawyerDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<UUID> prisonerIds;
    private String phoneNumber;
    private String company;
    private LawyerDegree degree;
    private String nationalId;
    private VisitStatus visitStatus;
    private VisitSchedule visitSchedule;
    private Role role;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private boolean isActive;
}
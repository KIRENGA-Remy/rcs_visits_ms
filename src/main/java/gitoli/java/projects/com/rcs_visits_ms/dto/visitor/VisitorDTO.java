package gitoli.java.projects.com.rcs_visits_ms.dto.visitor;

import gitoli.java.projects.com.rcs_visits_ms.dto.VisitScheduleDTO;
import gitoli.java.projects.com.rcs_visits_ms.enums.Role;
import gitoli.java.projects.com.rcs_visits_ms.enums.VisitStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitorDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String relationship;
    private Integer numberOfAccompanyingVisitors;
    private UUID prisonerId;
    private String phoneNumber;
    private String nationalId;
    private VisitScheduleDTO visitSchedule;
    private Role role;
    private VisitStatus visitStatus;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private boolean isActive;
}
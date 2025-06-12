package gitoli.java.projects.com.rcs_visits_ms.dto;

import gitoli.java.projects.com.rcs_visits_ms.entity.VisitSchedule;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitRequestDTO {

    @NotNull(message = "Visitor ID is required")
    private UUID visitorId;

    @NotNull(message = "Prisoner ID is required")
    private UUID prisonerId;

    @NotNull(message = "Visit schedule is required")
    private VisitScheduleDTO visitScheduleDTO;
}
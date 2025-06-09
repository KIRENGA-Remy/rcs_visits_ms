package gitoli.java.projects.com.rcs_visits_ms.dto.visitor;

import gitoli.java.projects.com.rcs_visits_ms.enums.VisitStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchVisitorDTO {
    private String name;
    private UUID prisonerId;
    private String prisonerName;
    private List<VisitStatus> visitStatuses;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private String visitScheduleLocation;
    private String visitScheduleRemarks;
    private LocalDate visitScheduleDate;
    private String visitScheduleTime;
}

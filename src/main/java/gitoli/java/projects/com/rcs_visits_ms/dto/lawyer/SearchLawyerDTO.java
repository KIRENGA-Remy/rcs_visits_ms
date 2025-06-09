package gitoli.java.projects.com.rcs_visits_ms.dto.lawyer;

import gitoli.java.projects.com.rcs_visits_ms.enums.LawyerDegree;
import gitoli.java.projects.com.rcs_visits_ms.enums.VisitStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchLawyerDTO {
    private String name;
    private UUID prisonerId;
    private String prisonerName;
    private List<VisitStatus> visitStatuses;
    private List<LawyerDegree> degrees;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private String visitScheduleLocation;
    private String visitScheduleRemarks;
    private LocalDate visitScheduleDate;
    private String visitScheduleTime;
}
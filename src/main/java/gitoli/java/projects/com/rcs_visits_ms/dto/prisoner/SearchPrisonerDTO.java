package gitoli.java.projects.com.rcs_visits_ms.dto.prisoner;

import gitoli.java.projects.com.rcs_visits_ms.enums.CourtStatus;
import gitoli.java.projects.com.rcs_visits_ms.enums.PrisonerStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchPrisonerDTO {
    private PrisonerStatus status;
    private String email;
    private CourtStatus courtStatus;
    private String nationality;
    private String name;
}
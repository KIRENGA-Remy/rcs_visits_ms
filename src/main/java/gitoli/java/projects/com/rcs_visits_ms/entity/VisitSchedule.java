package gitoli.java.projects.com.rcs_visits_ms.entity;

import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class VisitSchedule {
    private LocalDateTime visitDateTime;
    private String visitTime;
    private String location;
    private String remarks;
}
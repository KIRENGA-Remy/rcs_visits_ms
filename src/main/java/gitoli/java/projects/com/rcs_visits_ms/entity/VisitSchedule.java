package gitoli.java.projects.com.rcs_visits_ms.entity;

import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class VisitSchedule {
    @Column(name = "visit_date")
    private LocalDate visitDate;
    @Column(name = "visit_time")
    private String visitTime;
    @Column(name = "location")
    private String location;
    @Column(name = "remarks")
    private String remarks;
}
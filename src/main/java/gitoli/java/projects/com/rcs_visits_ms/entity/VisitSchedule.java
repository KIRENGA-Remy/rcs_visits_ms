package gitoli.java.projects.com.rcs_visits_ms.entity;

import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "visit_schedules", schema = "rcs_visits_ms")
public class VisitSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "visit_request_id", nullable = false)
    private VisitRequest visitRequest;

    @ManyToOne
    @JoinColumn(name = "prisoner_id", nullable = false)
    private Prisoner prisoner;

    @Column(name = "visit_date_time", nullable = false)
    private LocalDateTime visitDateTime;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
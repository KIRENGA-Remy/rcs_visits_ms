package gitoli.java.projects.com.rcs_visits_ms.entity;

import gitoli.java.projects.com.rcs_visits_ms.entity.admin.Admin;
import gitoli.java.projects.com.rcs_visits_ms.enums.SlipType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "slips", schema = "rcs_visits_ms")
public class Slip {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "visit_request_id", nullable = false)
    private VisitRequest visitRequest;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlipType slipType;

    @Column(name = "slip_file_path", nullable = false)
    private String slipFilePath;

    @Column(name = "generated_at", updatable = false, nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    private void onCreate() {
        this.generatedAt = LocalDateTime.now();
    }
    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
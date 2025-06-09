package gitoli.java.projects.com.rcs_visits_ms.entity;

import gitoli.java.projects.com.rcs_visits_ms.entity.admin.Admin;
import gitoli.java.projects.com.rcs_visits_ms.entity.lawyer.Lawyer;
import gitoli.java.projects.com.rcs_visits_ms.entity.visitor.Visitor;
import gitoli.java.projects.com.rcs_visits_ms.enums.SlipStatus;
import gitoli.java.projects.com.rcs_visits_ms.enums.SlipType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlipType slipType = SlipType.VISITOR_SLIP;

    @NotBlank
    @Column(nullable = false, unique = true, length = 50, name = "slip_number")
    private String slipNumber;

    @Column(name = "slip_file_path", nullable = false)
    private String slipFilePath;

    @NotBlank
    @Column(nullable = false, name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "slip_status")
    private SlipStatus slipStatus = SlipStatus.SENDING;

    @ManyToOne
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    @ManyToOne
    @JoinColumn(name = "lawyer_id")
    private Lawyer lawyer;

//    @OneToOne
//    @JoinColumn(name = "visit_request_id", nullable = false)
//    private VisitRequest visitRequest;

    @Column(name = "generated_at", updatable = false, nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean isActive = false;

    @PrePersist
    private void onCreate() {
        this.generatedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
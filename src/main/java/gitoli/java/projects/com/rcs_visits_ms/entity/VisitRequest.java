//package gitoli.java.projects.com.rcs_visits_ms.entity;
//
//import gitoli.java.projects.com.rcs_visits_ms.entity.admin.Admin;
//import gitoli.java.projects.com.rcs_visits_ms.entity.lawyer.Lawyer;
//import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
//import gitoli.java.projects.com.rcs_visits_ms.entity.visitor.Visitor;
//import gitoli.java.projects.com.rcs_visits_ms.enums.RequestStatus;
//import gitoli.java.projects.com.rcs_visits_ms.enums.VisitorType;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import lombok.*;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(name = "visit_requests", schema = "rcs_visits_ms")
//public class VisitRequest {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//    @ManyToOne
//    @JoinColumn(name = "prisoner_id", nullable = false)
//    private Prisoner prisoner;
//
//    @ManyToOne
//    @JoinColumn(name = "lawyer_id")
//    private Lawyer lawyer;
//
//    @ManyToOne
//    @JoinColumn(name = "visitor_id")
//    private Visitor visitor;
//
//    @NotNull
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private VisitorType visitorType = VisitorType.VISITOR;
//
//    @NotNull
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private RequestStatus status = RequestStatus.PENDING;
//
//    @Column(name = "requested_at", updatable = false, nullable = false)
//    private LocalDateTime requestedAt;
//
//    @ManyToOne
//    @JoinColumn(name = "approved_by")
//    private Admin approvedBy;
//
//    @Column(name = "approved_at")
//    private LocalDateTime approvedAt;
//
//    @Column(name = "rejection_reason")
//    private String rejectionReason;
//
//    @PrePersist
//    private void onCreate() {
//        this.requestedAt = LocalDateTime.now();
//    }
//}
package gitoli.java.projects.com.rcs_visits_ms.entity.visitor;

import gitoli.java.projects.com.rcs_visits_ms.entity.VisitSchedule;
import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
import gitoli.java.projects.com.rcs_visits_ms.enums.Role;
import gitoli.java.projects.com.rcs_visits_ms.enums.VisitStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "visitors", schema = "rcs_visits_ms")
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, name = "firstname", length = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, name = "lastname", length = 50)
    private String lastName;

    @Email
    @NotBlank
    @Size(max = 70)
    @Column(nullable = false, name = "email", length = 70, unique = true)
    private String email;

    @NotBlank
    @Size(max = 70)
    @Column(nullable = false, name = "password", length = 70)
    private String password;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, name = "relationship", length = 100)
    private String relationship;

    @NotNull
    @Column(nullable = false, name = "number_of_accompanying_visitors")
    private Integer numberOfAccompanyingVisitors;

    @ManyToOne
    @JoinColumn(name = "prisoner_id", nullable = false)
    private Prisoner prisoner;

    @NotBlank
    @Size(max = 15)
    @Column(nullable = false, name = "phone_number", length = 15)
    private String phoneNumber;

    @NotBlank
    @Size(max = 70)
    @Column(nullable = false, name = "national_id", unique = true, length = 70)
    private String nationalId;

    @Embedded
    private VisitSchedule visitSchedule;

    @NotNull
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.VISITOR;

    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "visit_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitStatus visitStatus = VisitStatus.PENDING;

    @Column(nullable = false)
    private boolean isActive = true;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
}
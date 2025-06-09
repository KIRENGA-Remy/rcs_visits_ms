package gitoli.java.projects.com.rcs_visits_ms.entity.lawyer;

import gitoli.java.projects.com.rcs_visits_ms.entity.VisitSchedule;
import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
import gitoli.java.projects.com.rcs_visits_ms.enums.LawyerDegree;
import gitoli.java.projects.com.rcs_visits_ms.enums.Role;
import gitoli.java.projects.com.rcs_visits_ms.enums.VisitStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lawyers", schema = "rcs_visits_ms")
public class Lawyer {
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

    @ManyToMany(mappedBy = "lawyers")
    @JoinColumn(name = "prisoner_id")
    private Set<Prisoner> prisoners;

    @NotBlank
    @Size(max = 15)
    @Column(nullable = false, name = "phone_number", length = 15)
    private String phoneNumber;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, name = "company", length = 100)
    private String company;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "degree")
    private LawyerDegree degree = LawyerDegree.Bachelor_of_Laws;

    @NotBlank
    @Size(max = 70)
    @Column(nullable = false, name = "national_id", unique = true, length = 70)
    private String nationalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_status", nullable = false)
    private VisitStatus visitStatus = VisitStatus.PENDING;

    @Embedded
    private VisitSchedule visitSchedule;

    @NotNull
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.LAWYER;

    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(nullable = false)
    private boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
}

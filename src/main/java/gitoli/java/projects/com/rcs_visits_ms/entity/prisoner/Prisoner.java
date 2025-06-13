package gitoli.java.projects.com.rcs_visits_ms.entity.prisoner;

import gitoli.java.projects.com.rcs_visits_ms.entity.lawyer.Lawyer;
import gitoli.java.projects.com.rcs_visits_ms.entity.visitor.Visitor;
import gitoli.java.projects.com.rcs_visits_ms.enums.CourtStatus;
import gitoli.java.projects.com.rcs_visits_ms.enums.Gender;
import gitoli.java.projects.com.rcs_visits_ms.enums.PrisonerStatus;
import gitoli.java.projects.com.rcs_visits_ms.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "prisoners", schema = "rcs_visits_ms")
public class Prisoner {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(min = 1)
    @Column(name = "prisonercode", unique = true, nullable = false)
    private String prisonerCode;

    @NotBlank
    @Size(max = 50)
    @Column(name = "firstname", nullable = false, length = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "lastname", nullable = false, length = 50)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 50)
    @Column(name = "email", unique = true, length = 50)
    private String email;

    @NotBlank
    @Size(max = 100)
    @Column(name = "password", length = 100)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @ManyToMany(mappedBy = "prisoners", cascade = CascadeType.ALL)
    private Set<Lawyer> lawyers = new HashSet<>();

    @OneToMany(mappedBy = "prisoner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Visitor> visitors = new HashSet<>();

    @Column(name = "date_of_imprisonment")
    private LocalDate dateOfImprisonment;

    @Column(name = "date_of_release")
    private LocalDate dateOfRelease;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PrisonerStatus status = PrisonerStatus.DETAINED;

    @Column(name = "court_report", columnDefinition = "TEXT")
    private String courtReport;

    @Enumerated(EnumType.STRING)
    @Column(name = "court_status")
    private CourtStatus courtStatus = CourtStatus.PENDING;

    @NotBlank
    @Column(name = "conviction_of_crime", nullable = false, columnDefinition = "TEXT")
    private String convictionOfCrime;

    @NotBlank
    @Size(max = 70)
    @Column(name = "national_id", nullable = false, unique = true, length = 70)
    private String nationalId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nationality", nullable = false, length = 50)
    private String nationality = "Rwandan";

    @Column(name = "health_details", columnDefinition = "TEXT")
    private String healthDetails;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.PRISONER;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "is_active", nullable = false)
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

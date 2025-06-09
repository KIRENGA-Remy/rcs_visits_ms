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

import java.sql.Timestamp;
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
    @Size(max = 50)
    @Column(nullable = false, name = "firstname", length = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, name = "lastname", length = 50)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 50)
    @Column(nullable = false, name = "email", unique = true, length = 50)
    private String email;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, name = "password", length = 50)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "gender")
    private Gender gender;

    @NotNull
    @Column(nullable = false, name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ManyToMany
    @JoinTable(
            name = "prisoner_lawyer",
            joinColumns = @JoinColumn(name = "prisoner_id"),
            inverseJoinColumns = @JoinColumn(name = "lawyer_id")
    )
    private Set<Lawyer> lawyers = new HashSet<>();

    @OneToMany(mappedBy = "prisoner")
    private Set<Visitor> visitors = new HashSet<>();

    @Column(name = "date_of_imprisonment")
    private LocalDate dateOfImprisonment;

    @Column(name = "date_of_release")
    private LocalDate dateOfRelease;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PrisonerStatus status = PrisonerStatus.DETAINED;

    @Lob
    @Column(name = "court_report")
    private String courtReport;

    @Enumerated(EnumType.STRING)
    @Column(name = "court_status")
    private CourtStatus courtStatus = CourtStatus.PENDING;

    @NotBlank
    @Column(columnDefinition = "TEXT", name = "conviction_of_crime", nullable = false)
    private String convictionOfCrime;

    @NotBlank
    @Size(max = 70)
    @Column(name = "national_id", unique = true, length = 70)
    private String nationalId;

    @NotBlank
    @Column(nullable = false, name = "nationality", length = 50)
    private String nationality = "Rwandan";

    @Lob
    @Column(name = "health_details")
    private String healthDetails;

    @NotNull
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.PRISONER;

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
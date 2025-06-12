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
import java.util.HashSet;
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
    @Column(name = "firstname", nullable = false, length = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "lastname", nullable = false, length = 50)
    private String lastName;

    @Email
    @NotBlank
    @Size(max = 70)
    @Column(name = "email", nullable = false, length = 70, unique = true)
    private String email;

    @NotBlank
    @Size(max = 70)
    @Column(name = "password", nullable = false, length = 70)
    private String password;

    @NotBlank
    @Size(max = 15)
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @NotBlank
    @Size(max = 100)
    @Column(name = "company", nullable = false, length = 100)
    private String company;

    @NotBlank
    @Size(max = 70)
    @Column(name = "national_id", nullable = false, unique = true, length = 70)
    private String nationalId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "degree", nullable = false)
    private LawyerDegree degree = LawyerDegree.Bachelor_of_Laws;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "visit_status", nullable = false)
    private VisitStatus visitStatus = VisitStatus.PENDING;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.LAWYER;

    @Embedded
    private VisitSchedule visitSchedule;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "lawyer_prisoner",
            schema = "rcs_visits_ms",
            joinColumns = @JoinColumn(name = "lawyer_id"),
            inverseJoinColumns = @JoinColumn(name = "prisoner_id")
    )
    private Set<Prisoner> prisoners = new HashSet<>();

    @Column(name = "created_at", updatable = false)
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

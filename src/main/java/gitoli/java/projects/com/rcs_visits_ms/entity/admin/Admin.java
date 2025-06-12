package gitoli.java.projects.com.rcs_visits_ms.entity.admin;

import gitoli.java.projects.com.rcs_visits_ms.enums.Role;
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
@Table(name = "admins", schema = "rcs_visits_ms")
public class Admin {
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
    @Column(nullable = false ,name = "email", length = 70, unique = true)
    private String email;

    @NotBlank
    @Size(max = 70)
    @Column(nullable = false, name = "password", length = 70)
    private String password;

    @NotBlank
    @Size(max = 15)
    @Column(nullable = false, name = "phone_number", length = 15)
    private String phoneNumber;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, name = "office_name", length = 100)
    private String officeName;

    @NotNull
    @Lob
    @Column(nullable = false)
    private byte[] signature;

    @NotNull
    @Lob
    @Column(nullable = false)
    private byte[] stamp;

    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ADMIN;

    @Column(nullable = false, name = "is_active")
    private boolean isActive= true;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDate.now();
    }
}

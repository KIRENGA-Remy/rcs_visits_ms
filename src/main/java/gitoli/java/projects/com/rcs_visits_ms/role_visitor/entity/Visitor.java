package gitoli.java.projects.com.rcs_visits_ms.role_visitor.entity;

import gitoli.java.projects.com.rcs_visits_ms.role_prisoner.entity.Prisoner;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "visitors")
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, name = "firstname", length = 50)
    private String firstName;
    @Column(nullable = false, name = "lastname", length = 50)
    private String lastName;
    @Column(name = "email", length = 70, unique = true)
    private String email;
    @Column(nullable = false, name = "password", length = 70)
    private String password;
    @Column(nullable = false, name = "relationship", length = 100)
    private String relationship;
    @Column(nullable = false, name = "number_of_visitors")
    private Integer numberOfVisitors;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "prisoner_id", nullable = false)
    private Prisoner prisoner;
    @Column(nullable = false, name = "phone_number", length = 15)
    private String phoneNumber;
    @Column(nullable = false, name = "national_id", unique = true, length = 70)
    private String nationalId;
}

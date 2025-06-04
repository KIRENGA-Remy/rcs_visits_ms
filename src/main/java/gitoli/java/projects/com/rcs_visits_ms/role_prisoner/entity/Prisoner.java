package gitoli.java.projects.com.rcs_visits_ms.role_prisoner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Prisoner {
    @Id
    @GeneratedValue
    private UUID id;
}

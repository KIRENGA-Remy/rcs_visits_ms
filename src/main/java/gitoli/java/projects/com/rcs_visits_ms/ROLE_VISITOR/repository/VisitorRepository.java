package gitoli.java.projects.com.rcs_visits_ms.ROLE_VISITOR.repository;

import gitoli.java.projects.com.rcs_visits_ms.ROLE_PRISONER.entity.Prisoner;
import gitoli.java.projects.com.rcs_visits_ms.ROLE_VISITOR.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, UUID> {
    Optional<Visitor> findByEmail(String email);
    Optional<Visitor> findByFirstName(String firstName);
    Optional<Visitor> findByLastName(String lastName);
    Optional<Visitor> findByNationalId(String nationalId);
    Optional<Visitor> findByPrisoner(Prisoner prisoner);
}

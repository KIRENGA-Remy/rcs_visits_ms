package gitoli.java.projects.com.rcs_visits_ms.repository.visitor;

import gitoli.java.projects.com.rcs_visits_ms.role_prisoner.entity.Prisoner;
import gitoli.java.projects.com.rcs_visits_ms.role_visitor.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, UUID> {
    Optional<Visitor> findByEmail(String email);
    Optional<Visitor> findByFirstName(String firstName);
    Optional<Visitor> findByLastName(String lastName);
    Optional<Visitor> findByNationalId(String nationalId);
    Optional<Visitor> findByPrisoner(Prisoner prisoner);
    List<Visitor> findAllByPrisonerId(UUID prisonerId);
    boolean existsByEmail(String email);
    boolean existsByNationalId(String nationalId);
}
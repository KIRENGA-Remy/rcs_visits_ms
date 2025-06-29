package gitoli.java.projects.com.rcs_visits_ms.repository.prisoner;

import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
import gitoli.java.projects.com.rcs_visits_ms.enums.CourtStatus;
import gitoli.java.projects.com.rcs_visits_ms.enums.Gender;
import gitoli.java.projects.com.rcs_visits_ms.enums.PrisonerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrisonerRepository extends JpaRepository<Prisoner, UUID> {
    Optional<Prisoner> findByNationalId(String nationalId);
    Optional<Prisoner> findByPrisonerCode(String prisonerCode);

    // Filtering by attributes
    Page<Prisoner> findByStatus(PrisonerStatus status, Pageable pageable);
    Page<Prisoner> findByCourtStatus(CourtStatus courtStatus, Pageable pageable);
    Page<Prisoner> findByNationality(String nationality, Pageable pageable);
    Page<Prisoner> findByGender(Gender gender, Pageable pageable);
    Page<Prisoner> findByDateOfImprisonmentBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Prisoner> findByDateOfReleaseBefore(LocalDate date, Pageable pageable);
    @Query(" SELECT p.prisonerCode FROM Prisoner p")
    List<String> findAllPrisonerCodes();

    //Searching by Name (Partial Match)
    @Query("SELECT p FROM Prisoner p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name ,'%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name ,'%'))")
    List<Prisoner> findByNameContainingIgnoreCase(@Param("name") String name);

    //Relationship-Based Queries
    @Query("SELECT p FROM Prisoner p JOIN p.lawyers l WHERE l.id = :lawyerId ")
    List<Prisoner> findByLawyerId(@Param("lawyerId") UUID lawyerId);
    @Query("SELECT p FROM Prisoner p JOIN p.visitors v WHERE v.id = :visitorId ")
    List<Prisoner> findByVisitorId(@Param("visitorId") UUID visitorId);

    //Paginated and Sorted Queries
    Page<Prisoner> findAllByStatus(PrisonerStatus status, Pageable pageable);
    Page<Prisoner> findAllByNationality(String nationality, Pageable pageable);

    //Active Prisoners Only
    List<Prisoner> findByIsActiveTrue();
    Page<Prisoner> findByIsActiveTrue(Pageable pageable);

    // Complex Search (e.g., for Admin Filtering)
    @Query("SELECT p FROM Prisoner p WHERE " +
            "(:status IS NULL OR LOWER(p.status ) LIKE LOWER(CONCAT('%', :status, '%'))) AND " +
            "(:courtStatus IS NULL OR LOWER(p.courtStatus) LIKE LOWER(CONCAT('%', :courtStatus, '%'))) AND " +
            "(:nationality IS NULL OR LOWER(p.nationality) LIKE LOWER(CONCAT('%', :nationality, '%'))) AND" +
            "(:name IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%' )) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Prisoner> searchPrisoners(
            @Param("status") PrisonerStatus status,
            @Param("courtStatus") CourtStatus courtStatus,
            @Param("nationality") String nationality,
            @Param("name") String name,
            Pageable pageable);

    //  Existence check
    boolean existsByNationality(String nationality);
    boolean existsByPrisonerCode(String prisonerCode);
}

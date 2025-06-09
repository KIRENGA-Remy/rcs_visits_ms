package gitoli.java.projects.com.rcs_visits_ms.repository.visitor;

import gitoli.java.projects.com.rcs_visits_ms.entity.VisitSchedule;
import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
import gitoli.java.projects.com.rcs_visits_ms.entity.visitor.Visitor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, UUID> {
    Optional<Visitor> findByEmail(String email);
    Optional<Visitor> findByNationalId(String nationalId);

    @Query("SELECT v FROM Visitor v WHERE LOWER(v.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(v.lastName) LIKE LOWER(CONCAT('%', :name, '%')) ")
    List<Visitor> findByName(@Param("name") String name);

    @Query("SELECT v FROM Visitor v JOIN v.prisoner p WHERE p.id = :prisonerId")
    List<Visitor> findByPrisonerId(@Param("prisonerId") UUID prisonerId);
    //Relationship-Based Queries
    @Query("SELECT v FROM Visitor v JOIN v.prisoner p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Visitor> findByPrisonerName(@Param("name") String name);

    @Query("select V FROM Visitor v WHERE LOWER(v.createdAt) LIKE LOWER(CONCAT('%', :createdAt, '%')) OR " +
            " LOWER(v.updatedAt) LIKE LOWER(CONCAT('%', :updatedAt, '%'))")
    Page<Visitor> findByCreatedAtAndUpdatedAt(
            @Param("createdAt") LocalDate createdAt,
            @Param("updatedAt") LocalDate updatedAt,
            Pageable pageable);

    @Query("SELECT v FROM Visitor v WHERE " +
            " (:location IS NULL OR LOWER(v.visitSchedule.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:remarks IS NULL OR LOWER(v.visitSchedule.remarks) LIKE LOWER(CONCAT('%', :remarks, '%'))) AND " +
            "(:visitDateTime IS NULL OR LOWER(v.visitSchedule.visitDateTime) LIKE LOWER(CONCAT('%', :visitDateTime, '%'))) AND " +
            "(:visitTime IS NULL OR LOWER(v.visitSchedule.visitTime) LIKE LOWER(CONCAT('%', :visitTime, '%')))")
    Page<Visitor> findByVisitSchedule(
            @Param("location") String location,
            @Param("remarks") String remarks,
            @Param("visitDateTime") LocalDate visitDateTime,
            @Param("visitTime") String visitTime,
            Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByNationalId(String nationalId);
}
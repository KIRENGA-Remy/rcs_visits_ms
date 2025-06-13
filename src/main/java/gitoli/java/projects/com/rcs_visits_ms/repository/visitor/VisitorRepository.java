package gitoli.java.projects.com.rcs_visits_ms.repository.visitor;

import gitoli.java.projects.com.rcs_visits_ms.entity.visitor.Visitor;
import gitoli.java.projects.com.rcs_visits_ms.enums.VisitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, UUID> {
    Optional<Visitor> findByEmail(String email);
    Optional<Visitor> findByNationalId(String nationalId);
    Optional<Visitor> findByPhoneNumber(String phoneNumber);

    List<Visitor> findByIsActiveTrue();
    Page<Visitor> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT v FROM Visitor v WHERE LOWER(v.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(v.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Visitor> findByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT v FROM Visitor v JOIN v.prisoner p WHERE p.id = :prisonerId")
    Page<Visitor> findByPrisonerId(@Param("prisonerId") UUID prisonerId, Pageable pageable);

    @Query("SELECT v FROM Visitor v JOIN v.prisoner p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Visitor> findByVisitorName(@Param("name") String name, Pageable pageable);

    @Query("SELECT v FROM Visitor v WHERE " +
            "(:createdAt IS NULL OR v.createdAt = :createdAt) AND " +
            "(:updatedAt IS NULL OR v.updatedAt = :updatedAt)")
    Page<Visitor> findByCreatedAtAndUpdatedAt(
            @Param("createdAt") LocalDate createdAt,
            @Param("updatedAt") LocalDate updatedAt,
            Pageable pageable);

    @Query("SELECT v FROM Visitor v WHERE " +
            "(:startDate IS NULL OR v.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR v.updatedAt <= :endDate)")
    Page<Visitor> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    @Query("SELECT v FROM Visitor v WHERE (:statuses IS NULL OR v.visitStatus IN :statuses)")
    Page<Visitor> findByVisitStatusIn(
            @Param("statuses") List<VisitStatus> statuses,
            Pageable pageable);

    @Query("SELECT v FROM Visitor v WHERE " +
            "(:location IS NULL OR LOWER(v.visitSchedule.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:remarks IS NULL OR LOWER(v.visitSchedule.remarks) LIKE LOWER(CONCAT('%', :remarks, '%'))) AND " +
            "(:visitDate IS NULL OR DATE(v.visitSchedule.visitDate) = :visitDate) AND " +
            "(:visitTime IS NULL OR LOWER(v.visitSchedule.visitTime) LIKE LOWER(CONCAT('%', :visitTime, '%')))")
    Page<Visitor> findByVisitSchedule(
            @Param("location") String location,
            @Param("remarks") String remarks,
            @Param("visitDate") LocalDate visitDate,
            @Param("visitTime") String visitTime,
            Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByNationalId(String nationalId);
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByIdAndIsActiveTrue(UUID id);
}
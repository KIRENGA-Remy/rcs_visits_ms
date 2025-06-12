package gitoli.java.projects.com.rcs_visits_ms.repository.lawyer;


import gitoli.java.projects.com.rcs_visits_ms.entity.lawyer.Lawyer;
import gitoli.java.projects.com.rcs_visits_ms.enums.LawyerDegree;
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
public interface LawyerRepository extends JpaRepository<Lawyer, UUID> {
    Optional<Lawyer> findByEmail(String email);
    Optional<Lawyer> findByNationalId(String nationalId);
    Optional<Lawyer> findByPhoneNumber(String phoneNumber);
    Optional<Lawyer> findByCompany(String company);

    List<Lawyer> findByIsActiveTrue();
    Page<Lawyer> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT l FROM Lawyer l WHERE LOWER(l.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(l.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Lawyer> findByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT l FROM Lawyer l JOIN l.prisoners p WHERE p.id = :prisonerId")
    Page<Lawyer> findByPrisonerId(@Param("prisonerId") UUID prisonerId, Pageable pageable);

    @Query("SELECT l FROM Lawyer l JOIN l.prisoners p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Lawyer> findByPrisonerName(@Param("name") String name, Pageable pageable);

    @Query("SELECT l FROM Lawyer l WHERE " +
            "(:createdAt IS NULL OR l.createdAt = :createdAt) AND " +
            "(:updatedAt IS NULL OR l.updatedAt = :updatedAt)")
    Page<Lawyer> findByCreatedAtAndUpdatedAt(
            @Param("createdAt") LocalDate createdAt,
            @Param("updatedAt") LocalDate updatedAt,
            Pageable pageable);

    @Query("SELECT l FROM Lawyer l WHERE " +
            "(:startDate IS NULL OR l.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR l.updatedAt <= :endDate)")
    Page<Lawyer> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    @Query("SELECT l FROM Lawyer l WHERE (:status IS NULL OR l.visitStatus IN :statuses)")
    Page<Lawyer> findByVisitStatusIn(
            @Param("statuses") List<VisitStatus> statuses,
            Pageable pageable
            );

    @Query("SELECT l FROM Lawyer l WHERE (:degrees IS NULL OR l.degree IN :degrees)")
    Page<Lawyer> findByLawyerDegreeIn(
            @Param("degrees") List<LawyerDegree> degrees,
            Pageable pageable
    );

    @Query("SELECT l FROM Lawyer l WHERE " +
            " (:location IS NULL OR LOWER(l.visitSchedule.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:remarks IS NULL OR LOWER(l.visitSchedule.remarks) LIKE LOWER(CONCAT('%', :remarks, '%'))) AND " +
            "(:visitDateTime IS NULL OR DATE(l.visitSchedule.visitDateTime) = :visitDateTime) AND " +
            "(:visitTime IS NULL OR LOWER(l.visitSchedule.visitTime) LIKE LOWER(CONCAT('%', :visitTime, '%')))")
    Page<Lawyer> findByVisitSchedule(
            @Param("location") String location,
            @Param("remarks") String remarks,
            @Param("visitDateTime") LocalDate visitDateTime,
            @Param("visitTime") String visitTime,
            Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByNationalId(String nationalId);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByCompany(String company);
}
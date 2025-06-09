package gitoli.java.projects.com.rcs_visits_ms.repository.admin;

import gitoli.java.projects.com.rcs_visits_ms.entity.admin.Admin;
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
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByNationalId(String nationalId);
    Optional<Admin> findByPhoneNumber(String phoneNumber);
    Optional<Admin> findByOfficeName(String officeName);

    List<Admin> findByIsActiveTrue();
    Page<Admin> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT a FROM Admin a WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Admin> findByName(@Param("name") String name);

    @Query("SELECT a FROM Admin a WHERE " +
            "(:createdAt IS NULL OR a.createdAt = :createdAt) AND " +
            "(:updatedAt IS NULL OR a.updatedAt = :updatedAt)")
    Page<Admin> findByCreatedAtAndUpdatedAt(
            @Param("createdAt") LocalDate createdAt,
            @Param("updatedAt") LocalDate updatedAt,
            Pageable pageable);

    @Query("SELECT a FROM Admin a WHERE " +
            "(:startDate IS NULL OR a.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR a.updatedAt <= :endDate)")
    Page<Admin> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    @Query("SELECT l FROM Lawyer l WHERE " +
            " (:location IS NULL OR LOWER(l.visitSchedule.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:remarks IS NULL OR LOWER(l.visitSchedule.remarks) LIKE LOWER(CONCAT('%', :remarks, '%'))) AND " +
            "(:visitDateTime IS NULL OR DATE(l.visitSchedule.visitDateTime) = :visitDateTime ) AND " +
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
    boolean existsByOfficeName(String officeName);
}

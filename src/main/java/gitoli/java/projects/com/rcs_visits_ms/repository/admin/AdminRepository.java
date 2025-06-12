package gitoli.java.projects.com.rcs_visits_ms.repository.admin;

import gitoli.java.projects.com.rcs_visits_ms.entity.admin.Admin;
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
public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByPhoneNumber(String phoneNumber);
    Optional<Admin> findByOfficeName(String officeName);

    List<Admin> findByIsActiveTrue();
    Page<Admin> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT a FROM Admin a WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Admin> findByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT a FROM Admin a WHERE LOWER(a.officeName) LIKE LOWER(CONCAT('%', :officeName, '%'))")
    Page<Admin> findByOfficeName(@Param("officeName") String officeName, Pageable pageable);

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

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByOfficeName(String officeName);
}
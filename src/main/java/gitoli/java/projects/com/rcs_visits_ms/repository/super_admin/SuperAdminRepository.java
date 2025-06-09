package gitoli.java.projects.com.rcs_visits_ms.repository.super_admin;

import gitoli.java.projects.com.rcs_visits_ms.entity.super_admin.SuperAdmin;
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
public interface SuperAdminRepository extends JpaRepository<SuperAdmin, UUID> {
    Optional<SuperAdmin> findByEmail(String email);
    Optional<SuperAdmin> findByPhoneNumber(String phoneNumber);
    Optional<SuperAdmin> findByOfficeName(String officeName);

    List<SuperAdmin> findByIsActiveTrue();
    Page<SuperAdmin> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT sa FROM SuperAdmin sa WHERE LOWER(sa.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(sa.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<SuperAdmin> findByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT sa FROM SuperAdmin sa WHERE " +
            "(:createdAt IS NULL OR sa.createdAt = :createdAt) AND " +
            "(:updatedAt IS NULL OR sa.updatedAt = :updatedAt)")
    Page<SuperAdmin> findByCreatedAtAndUpdatedAt(
            @Param("createdAt") LocalDate createdAt,
            @Param("updatedAt") LocalDate updatedAt,
            Pageable pageable);

    @Query("SELECT sa FROM SuperAdmin sa WHERE " +
            "(:startDate IS NULL OR sa.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR sa.updatedAt <= :endDate)")
    Page<SuperAdmin> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByOfficeName(String officeName);
}
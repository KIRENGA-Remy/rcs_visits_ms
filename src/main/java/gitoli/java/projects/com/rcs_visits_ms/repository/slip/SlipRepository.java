package gitoli.java.projects.com.rcs_visits_ms.repository.slip;

import gitoli.java.projects.com.rcs_visits_ms.entity.Slip;
import gitoli.java.projects.com.rcs_visits_ms.entity.lawyer.Lawyer;
import gitoli.java.projects.com.rcs_visits_ms.enums.SlipStatus;
import gitoli.java.projects.com.rcs_visits_ms.enums.SlipType;
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
public interface SlipRepository extends JpaRepository<Slip, UUID> {
    Optional<Slip> findBySlipNumber(String slipNumber);
    List<Slip> findByIsActiveFalse();
    Page<Slip> findByIsActiveFalse(Pageable pageable);

    @Query(" SELECT s FROM Slip s JOIN s.visitor v WHERE v.id = :visitorId")
    List<Slip> findByVisitorId(UUID visitorId);

    @Query(" SELECT s FROM Slip s JOIN s.lawyer l WHERE l.id = :lawyerId")
    List<Slip> findByLawyerId(UUID lawyerId);

    @Query(" SELECT s FROM Slip s JOIN s.admin a WHERE a.id = :adminId")
    Page<Slip> findByAdminId(UUID adminId, Pageable pageable);

    @Query(" SELECT s FROM Slip s WHERE (:statuses IS NULL OR s.slipStatus IN :statuses)")
    Page<Slip> findBySlipStatus(
            @Param("statuses") List<SlipStatus> statuses,
            Pageable pageable);

    @Query(" SELECT s FROM Slip s WHERE (:types IS NULL OR s.slipStatus IN :types)")
    Page<Slip> findBySlipType(
            @Param("types") List<SlipType> types,
            Pageable pageable);

    @Query(" SELECT s FROM Slip s WHERE (:startDate IS NULL OR s.generatedAt >= :startDate) AND" +
            "(:endDate IS NULL OR s.updatedAt <= :endDate)")
    Page<Slip> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}

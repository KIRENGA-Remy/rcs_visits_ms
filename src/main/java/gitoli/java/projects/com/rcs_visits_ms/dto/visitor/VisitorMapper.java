package gitoli.java.projects.com.rcs_visits_ms.dto.visitor;

import gitoli.java.projects.com.rcs_visits_ms.dto.CreateVisitorDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.VisitScheduleDTO;
import gitoli.java.projects.com.rcs_visits_ms.entity.VisitSchedule;
import gitoli.java.projects.com.rcs_visits_ms.entity.visitor.Visitor;
import gitoli.java.projects.com.rcs_visits_ms.enums.VisitStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class VisitorMapper {

    public VisitorDTO toDTO(Visitor visitor) {
        if (visitor == null) {
            return null;
        }

        return VisitorDTO.builder()
                .id(visitor.getId())
                .nationalId(visitor.getNationalId())
                .firstName(visitor.getFirstName())
                .lastName(visitor.getLastName())
                .email(visitor.getEmail())
                .relationship(visitor.getRelationship())
                .numberOfAccompanyingVisitors(visitor.getNumberOfAccompanyingVisitors())
                .prisonerId(visitor.getPrisoner() != null ? visitor.getPrisoner().getId() : null)
                .phoneNumber(visitor.getPhoneNumber())
                .visitScheduleDTO(toVisitScheduleDTO(visitor.getVisitSchedule()))
                .visitStatus(visitor.getVisitStatus())
                .role(visitor.getRole())
                .createdAt(visitor.getCreatedAt())
                .updatedAt(visitor.getUpdatedAt())
                .isActive(visitor.isActive())
                .build();
    }

    public VisitScheduleDTO toVisitScheduleDTO(VisitSchedule visitSchedule) {
        if (visitSchedule == null) {
            System.out.println("visitScheduleDTO is null");
            return null;
        }

        return VisitScheduleDTO.builder()
                .location(visitSchedule.getLocation())
                .visitDate(visitSchedule.getVisitDate() != null ? visitSchedule.getVisitDate() : null)
                .visitTime(visitSchedule.getVisitTime())
                .remarks(visitSchedule.getRemarks())
                .build();
    }

    public Visitor toEntity(CreateVisitorDTO dto) {
        if (dto == null) {
            return null;
        }

        return Visitor.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .relationship(dto.getRelationship())
                .numberOfAccompanyingVisitors(dto.getNumberOfAccompanyingVisitors())
                .phoneNumber(dto.getPhoneNumber())
                .nationalId(dto.getNationalId())
                .visitSchedule(toVisitSchedule(dto.getVisitScheduleDTO()))
                .role(dto.getRole())
                .visitStatus(dto.getVisitStatus() != null ? dto.getVisitStatus() : VisitStatus.PENDING)
                .isActive(true)
                .build();
    }

    public VisitSchedule toVisitSchedule(VisitScheduleDTO dto) {
        if (dto == null) {
            return null;
        }

        VisitSchedule visitSchedule = new VisitSchedule();
        visitSchedule.setLocation(dto.getLocation());
//        visitSchedule.setVisitDate(dto.getVisitDate() != null ? dto.getVisitDate().atStartOfDay() : null);
        try {
            visitSchedule.setVisitDate(dto.getVisitDate() != null ? dto.getVisitDate() : null);
            System.out.println("visitDate set to: " + visitSchedule.getVisitDate());
        } catch (Exception e) {
            System.err.println("Error parsing visitDate: " + dto.getVisitDate() + ", " + e.getMessage());
            throw new IllegalArgumentException("Invalid visitDate format: " + dto.getVisitDate(), e);
        }
        visitSchedule.setVisitTime(dto.getVisitTime());
        visitSchedule.setRemarks(dto.getRemarks());
        return visitSchedule;
    }
}
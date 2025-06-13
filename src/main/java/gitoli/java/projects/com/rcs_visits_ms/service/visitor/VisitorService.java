package gitoli.java.projects.com.rcs_visits_ms.service.visitor;

import gitoli.java.projects.com.rcs_visits_ms.dto.CreateVisitorDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.LoginDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.VisitRequestDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.prisoner.PrisonerDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.VisitScheduleDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.prisoner.PrisonerMapper;
import gitoli.java.projects.com.rcs_visits_ms.dto.visitor.VisitorDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.visitor.VisitorMapper;
import gitoli.java.projects.com.rcs_visits_ms.entity.VisitSchedule;
import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
import gitoli.java.projects.com.rcs_visits_ms.entity.visitor.Visitor;
import gitoli.java.projects.com.rcs_visits_ms.enums.VisitStatus;
import gitoli.java.projects.com.rcs_visits_ms.repository.prisoner.PrisonerRepository;
import gitoli.java.projects.com.rcs_visits_ms.repository.visitor.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final PrisonerRepository prisonerRepository;
    private final PasswordEncoder passwordEncoder;
    private final VisitorMapper visitorMapper;
    private final PrisonerMapper prisonerMapper;

    @Transactional
    public VisitorDTO registerVisitor(CreateVisitorDTO dto) {
        if (visitorRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (visitorRepository.existsByNationalId(dto.getNationalId())) {
            throw new IllegalArgumentException("National ID already exists");
        }
        if (visitorRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        Prisoner prisoner = prisonerRepository.findById(dto.getPrisonerId())
                .orElseThrow(() -> new IllegalArgumentException("Prisoner not found with ID: " + dto.getPrisonerId()));

        Visitor visitor = visitorMapper.toEntity(dto);
        visitor.setPrisoner(prisoner);
        visitor.setPassword(passwordEncoder.encode(dto.getPassword()));
        visitorRepository.save(visitor);
        return visitorMapper.toDTO(visitor);
    }

    @Transactional
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('VISITOR') and #id == principal.id")
    public VisitorDTO updateVisitor(UUID id, CreateVisitorDTO dto) {
        Visitor visitor = visitorRepository.findById(id)
                .filter(Visitor::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Active visitor not found with ID: " + id));

        visitorRepository.findByEmail(dto.getEmail())
                .filter(v -> !v.getId().equals(id))
                .ifPresent(v -> {
                    throw new IllegalArgumentException("Email already exists");
                });
        visitorRepository.findByNationalId(dto.getNationalId())
                .filter(v -> !v.getId().equals(id))
                .ifPresent(v -> {
                    throw new IllegalArgumentException("National ID already exists");
                });
        visitorRepository.findByPhoneNumber(dto.getPhoneNumber())
                .filter(v -> !v.getId().equals(id))
                .ifPresent(v -> {
                    throw new IllegalArgumentException("Phone number already exists");
                });

        Prisoner prisoner = prisonerRepository.findById(dto.getPrisonerId())
                .orElseThrow(() -> new IllegalArgumentException("Prisoner not found with ID: " + dto.getPrisonerId()));

        visitor.setFirstName(dto.getFirstName());
        visitor.setLastName(dto.getLastName());
        visitor.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            visitor.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        visitor.setRelationship(dto.getRelationship());
        visitor.setNumberOfAccompanyingVisitors(dto.getNumberOfAccompanyingVisitors());
        visitor.setPrisoner(prisoner);
        visitor.setPhoneNumber(dto.getPhoneNumber());
        visitor.setNationalId(dto.getNationalId());
        visitor.setVisitSchedule(visitorMapper.toVisitSchedule(dto.getVisitScheduleDTO()));
        visitor.setRole(dto.getRole());
        visitor.setVisitStatus(dto.getVisitStatus() != null ? dto.getVisitStatus() : VisitStatus.PENDING);

        visitorRepository.save(visitor);
        return visitorMapper.toDTO(visitor);
    }

    public VisitorDTO loginVisitor(LoginDTO dto) {
        Visitor visitor = visitorRepository.findByEmail(dto.getEmail())
                .filter(Visitor::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), visitor.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return visitorMapper.toDTO(visitor);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('VISITOR') and #id == principal.id")
    public VisitorDTO getVisitorDetails(UUID id) {
        Visitor visitor = visitorRepository.findById(id)
                .filter(Visitor::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Active visitor not found with ID: " + id));
        return visitorMapper.toDTO(visitor);
    }

    @PreAuthorize("hasRole('VISITOR') and #visitorId == principal.id")
    public byte[] generateVisitSlip(UUID visitorId) {
        Visitor visitor = visitorRepository.findById(visitorId)
                .filter(Visitor::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Active visitor not found with ID: " + visitorId));

        if (visitor.getVisitSchedule() == null) {
            throw new IllegalStateException("No visit schedule available for slip generation");
        }

        // Implement PDF generation logic using a library like iText
        String slipContent = String.format(
                "Visit Slip\nVisitor: %s %s\nPrisoner: %s %s\nVisit Date: %s\nVisit Time: %s\nLocation: %s\nStatus: %s",
                visitor.getFirstName(), visitor.getLastName(),
                visitor.getPrisoner().getFirstName(), visitor.getPrisoner().getLastName(),
                visitor.getVisitSchedule().getVisitDate(),
                visitor.getVisitSchedule().getVisitTime(),
                visitor.getVisitSchedule().getLocation(),
                visitor.getVisitStatus()
        );

        // TODO: Replace with actual PDF generation (e.g., iText or Apache PDFBox)
        return slipContent.getBytes();
    }

    @PreAuthorize("hasRole('VISITOR') and #visitorId == principal.id")
    public VisitScheduleDTO getVisitSchedule(UUID visitorId) {
        Visitor visitor = visitorRepository.findById(visitorId)
                .filter(Visitor::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Active visitor not found with ID: " + visitorId));
        return visitorMapper.toVisitScheduleDTO(visitor.getVisitSchedule());
    }

    @PreAuthorize("hasRole('VISITOR') and #visitorId == principal.id")
    public PrisonerDTO getPrisonerDetails(UUID visitorId) {
        Visitor visitor = visitorRepository.findById(visitorId)
                .filter(Visitor::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Active visitor not found with ID: " + visitorId));
        return prisonerMapper.toDTO(visitor.getPrisoner());
    }

    @Transactional
    @PreAuthorize("hasRole('VISITOR') and #dto.visitorId == principal.id")
    public VisitorDTO requestVisit(VisitRequestDTO dto) {
        Visitor visitor = visitorRepository.findById(dto.getVisitorId())
                .filter(Visitor::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Active visitor not found with ID: " + dto.getVisitorId()));
        Prisoner prisoner = prisonerRepository.findById(dto.getPrisonerId())
                .orElseThrow(() -> new IllegalArgumentException("Prisoner not found with ID: " + dto.getPrisonerId()));

        if (!visitor.getPrisoner().getId().equals(dto.getPrisonerId())) {
            throw new IllegalArgumentException("Visitor is not associated with the specified prisoner");
        }

        visitor.setVisitSchedule(visitorMapper.toVisitSchedule(dto.getVisitScheduleDTO()));
        visitor.setVisitStatus(VisitStatus.PENDING);
        visitorRepository.save(visitor);
        return visitorMapper.toDTO(visitor);
    }

    @Transactional
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public VisitorDTO approveVisitRequest(UUID visitorId) {
        Visitor visitor = visitorRepository.findById(visitorId)
                .filter(Visitor::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Active visitor not found with ID: " + visitorId));
        visitor.setVisitStatus(VisitStatus.APPROVED);
        visitorRepository.save(visitor);
        return visitorMapper.toDTO(visitor);
    }

    @Transactional
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public VisitorDTO rejectVisitRequest(UUID visitorId) {
        Visitor visitor = visitorRepository.findById(visitorId)
                .filter(Visitor::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Active visitor not found with ID: " + visitorId));
        visitor.setVisitStatus(VisitStatus.REJECTED);
        visitorRepository.save(visitor);
        return visitorMapper.toDTO(visitor);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public Page<VisitorDTO> getVisitors(Pageable pageable) {
        return visitorRepository.findByIsActiveTrue(pageable)
                .map(visitorMapper::toDTO);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public Page<VisitorDTO> searchVisitorsByPrisonerId(UUID prisonerId, Pageable pageable) {
        return visitorRepository.findByPrisonerId(prisonerId, pageable)
                .map(visitorMapper::toDTO);
    }

    private VisitSchedule mapToVisitSchedule(VisitScheduleDTO dto) {
        if (dto == null) return null;

        VisitSchedule visitSchedule = new VisitSchedule();
        visitSchedule.setLocation(dto.getLocation());
        visitSchedule.setVisitDate(dto.getVisitDate() != null ? dto.getVisitDate() : null);
        visitSchedule.setVisitTime(dto.getVisitTime());
        visitSchedule.setRemarks(dto.getRemarks());
        return visitSchedule;
    }
}
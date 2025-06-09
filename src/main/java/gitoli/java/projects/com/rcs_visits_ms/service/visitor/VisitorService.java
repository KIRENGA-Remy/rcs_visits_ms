package gitoli.java.projects.com.rcs_visits_ms.service.visitor;

import gitoli.java.projects.com.rcs_visits_ms.dto.visitor.CreateVisitorDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.visitor.SearchVisitorDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.visitor.VisitorDTO;
import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
import gitoli.java.projects.com.rcs_visits_ms.entity.visitor.Visitor;
import gitoli.java.projects.com.rcs_visits_ms.repository.prisoner.PrisonerRepository;
import gitoli.java.projects.com.rcs_visits_ms.repository.visitor.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final PrisonerRepository prisonerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public VisitorDTO createVisitor(CreateVisitorDTO dto) {
        if (visitorRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (visitorRepository.existsByNationalId(dto.getNationalId())) {
            throw new IllegalArgumentException("National ID already exists");
        }
        if (visitorRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        Visitor visitor = mapToEntity(dto);
        visitor.setPassword(passwordEncoder.encode(dto.getPassword()));
        visitor = visitorRepository.save(visitor);
        return mapToDTO(visitor);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public VisitorDTO updateVisitor(UUID id, CreateVisitorDTO dto) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visitor not found with ID: " + id));

        // Check for duplicates (excluding current visitor)
        visitorRepository.findByEmail(dto.getEmail())
                .filter(v -> !v.getId().equals(id))
                .ifPresent(v -> { throw new IllegalArgumentException("Email already exists"); });
        visitorRepository.findByNationalId(dto.getNationalId())
                .filter(v -> !v.getId().equals(id))
                .ifPresent(v -> { throw new IllegalArgumentException("National ID already exists"); });
        visitorRepository.findByPhoneNumber(dto.getPhoneNumber())
                .filter(v -> !v.getId().equals(id))
                .ifPresent(v -> { throw new IllegalArgumentException("Phone number already exists"); });

        visitor.setFirstName(dto.getFirstName());
        visitor.setLastName(dto.getLastName());
        visitor.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            visitor.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        visitor.setRelationship(dto.getRelationship());
        visitor.setNumberOfAccompanyingVisitors(dto.getNumberOfAccompanyingVisitors());
        visitor.setPrisoner(fetchPrisoner(dto.getPrisonerId()));
        visitor.setPhoneNumber(dto.getPhoneNumber());
        visitor.setNationalId(dto.getNationalId());
        visitor.setVisitSchedule(dto.getVisitSchedule());
        visitor.setRole(dto.getRole());
        visitor.setVisitStatus(dto.getVisitStatus() != null ? dto.getVisitStatus() : VisitStatus.PENDING);

        visitor = visitorRepository.save(visitor);
        return mapToDTO(visitor);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('VISITOR') or hasRole('SUPER_ADMIN') and #id == principal.id)")
    public VisitorDTO getVisitor(UUID id) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visitor not found with ID: " + id));
        return mapToDTO(visitor);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') ")
    public void deleteVisitor(UUID id) {
        if (!visitorRepository.existsById(id)) {
            throw new IllegalArgumentException("Visitor not found with ID: " + id);
        }
        visitorRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') ")
    public Page<VisitorDTO> searchVisitors(SearchVisitorDTO dto, Pageable pageable) {
        if (dto.getPrisonerId() != null) {
            return visitorRepository.findByPrisonerId(dto.getPrisonerId(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getPrisonerName() != null && !dto.getPrisonerName().isBlank()) {
            return visitorRepository.findByPrisonerName(dto.getPrisonerName(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getVisitStatuses() != null && !dto.getVisitStatuses().isEmpty()) {
            return visitorRepository.findByVisitStatusIn(dto.getVisitStatuses(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getCreatedAt() != null || dto.getUpdatedAt() != null) {
            return visitorRepository.findByCreatedAtAndUpdatedAt(dto.getCreatedAt(), dto.getUpdatedAt(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getStartDate() != null || dto.getEndDate() != null) {
            return visitorRepository.findByDateRange(dto.getStartDate(), dto.getEndDate(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getVisitScheduleLocation() != null || dto.getVisitScheduleRemarks() != null ||
                dto.getVisitScheduleDate() != null || dto.getVisitScheduleTime() != null) {
            return visitorRepository.findByVisitSchedule(
                            dto.getVisitScheduleLocation(), dto.getVisitScheduleRemarks(),
                            dto.getVisitScheduleDate(), dto.getVisitScheduleTime(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getName() != null && !dto.getName().isBlank()) {
            return visitorRepository.findByName(dto.getName(), pageable)
                    .map(this::mapToDTO);
        }
        return visitorRepository.findByIsActiveTrue(pageable).map(this::mapToDTO);
    }

    @PreAuthorize("hasRole('VISITOR') or hasRole('SUPER_ADMIN') ")
    public VisitorDTO getVisitorByEmail(String email) {
        Visitor visitor = visitorRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Visitor not found with email: " + email));
        return mapToDTO(visitor);
    }

    private Visitor mapToEntity(CreateVisitorDTO dto) {
        return Visitor.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .relationship(dto.getRelationship())
                .numberOfAccompanyingVisitors(dto.getNumberOfAccompanyingVisitors())
                .prisoner(fetchPrisoner(dto.getPrisonerId()))
                .phoneNumber(dto.getPhoneNumber())
                .nationalId(dto.getNationalId())
                .visitSchedule(dto.getVisitSchedule())
                .role(dto.getRole())
                .visitStatus(dto.getVisitStatus() != null ? dto.getVisitStatus() : VisitStatus.PENDING)
                .isActive(true)
                .build();
    }

    private VisitorDTO mapToDTO(Visitor visitor) {
        return VisitorDTO.builder()
                .id(visitor.getId())
                .firstName(visitor.getFirstName())
                .lastName(visitor.getLastName())
                .email(visitor.getEmail())
                .relationship(visitor.getRelationship())
                .numberOfAccompanyingVisitors(visitor.getNumberOfAccompanyingVisitors())
                .prisonerId(visitor.getPrisoner().getId())
                .phoneNumber(visitor.getPhoneNumber())
                .nationalId(visitor.getNationalId())
                .visitSchedule(visitor.getVisitSchedule())
                .role(visitor.getRole())
                .createdAt(visitor.getCreatedAt())
                .updatedAt(visitor.getUpdatedAt())
                .visitStatus(visitor.getVisitStatus())
                .isActive(visitor.isActive())
                .build();
    }

    private Prisoner fetchPrisoner(UUID prisonerId) {
        return prisonerRepository.findById(prisonerId)
                .orElseThrow(() -> new IllegalArgumentException("Prisoner not found with ID: " + prisonerId));
    }
}
package gitoli.java.projects.com.rcs_visits_ms.service.lawyer;

import gitoli.java.projects.com.rcs_visits_ms.dto.lawyer.CreateLawyerDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.lawyer.LawyerDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.lawyer.SearchLawyerDTO;
import gitoli.java.projects.com.rcs_visits_ms.entity.lawyer.Lawyer;
import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
import gitoli.java.projects.com.rcs_visits_ms.repository.lawyer.LawyerRepository;
import gitoli.java.projects.com.rcs_visits_ms.repository.prisoner.PrisonerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LawyerService {
    private final LawyerRepository lawyerRepository;
    private final PrisonerRepository prisonerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') ")
    public LawyerDTO createLawyer(CreateLawyerDTO dto) {
        if (lawyerRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (lawyerRepository.existsByNationalId(dto.getNationalId())) {
            throw new IllegalArgumentException("National ID already exists");
        }
        if (lawyerRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }
        if (lawyerRepository.existsByCompanyName(dto.getCompany())) {
            throw new IllegalArgumentException("Company name already exists");
        }

        Lawyer lawyer = mapToEntity(dto);
        lawyer.setPassword(passwordEncoder.encode(dto.getPassword()));
        lawyer = lawyerRepository.save(lawyer);
        return mapToDTO(lawyer);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') ")
    public LawyerDTO updateLawyer(UUID id, CreateLawyerDTO dto) {
        Lawyer lawyer = lawyerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lawyer not found with ID: " + id));

        // Check for duplicates (excluding current lawyer)
        lawyerRepository.findByEmail(dto.getEmail())
                .filter(l -> !l.getId().equals(id))
                .ifPresent(l -> { throw new IllegalArgumentException("Email already exists"); });
        lawyerRepository.findByNationalId(dto.getNationalId())
                .filter(l -> !l.getId().equals(id))
                .ifPresent(l -> { throw new IllegalArgumentException("National ID already exists"); });
        lawyerRepository.findByPhoneNumber(dto.getPhoneNumber())
                .filter(l -> !l.getId().equals(id))
                .ifPresent(l -> { throw new IllegalArgumentException("Phone number already exists"); });
        lawyerRepository.findByCompanyName(dto.getCompany())
                .filter(l -> !l.getId().equals(id))
                .ifPresent(l -> { throw new IllegalArgumentException("Company name already exists"); });

        lawyer.setFirstName(dto.getFirstName());
        lawyer.setLastName(dto.getLastName());
        lawyer.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            lawyer.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        lawyer.setPrisoners(fetchPrisoners(dto.getPrisonerIds()));
        lawyer.setPhoneNumber(dto.getPhoneNumber());
        lawyer.setCompany(dto.getCompany());
        lawyer.setDegree(dto.getDegree());
        lawyer.setNationalId(dto.getNationalId());
        lawyer.setVisitStatus(dto.getVisitStatus() != null ? dto.getVisitStatus() : VisitStatus.PENDING);
        lawyer.setVisitSchedule(dto.getVisitSchedule());
        lawyer.setRole(dto.getRole());

        lawyer = lawyerRepository.save(lawyer);
        return mapToDTO(lawyer);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or (hasRole('LAWYER') and #id == principal.id)")
    public LawyerDTO getLawyer(UUID id) {
        Lawyer lawyer = lawyerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lawyer not found with ID: " + id));
        return mapToDTO(lawyer);
    }

    @PreAuthorize("hasRole('LAWYER') or hasRole('SUPER_ADMIN') ")
    public LawyerDTO getLawyerByEmail(String email) {
        Lawyer lawyer = lawyerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Lawyer not found with email: " + email));
        return mapToDTO(lawyer);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') ")
    public void deleteLawyer(UUID id) {
        if (!lawyerRepository.existsById(id)) {
            throw new IllegalArgumentException("Lawyer not found with ID: " + id);
        }
        lawyerRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') ")
    public Page<LawyerDTO> searchLawyers(SearchLawyerDTO dto, Pageable pageable) {
        if (dto.getPrisonerId() != null) {
            return lawyerRepository.findByPrisonerId(dto.getPrisonerId(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getPrisonerName() != null && !dto.getPrisonerName().isBlank()) {
            return lawyerRepository.findByPrisonerName(dto.getPrisonerName(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getVisitStatuses() != null && !dto.getVisitStatuses().isEmpty()) {
            return lawyerRepository.findByVisitStatusIn(dto.getVisitStatuses(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getDegrees() != null && !dto.getDegrees().isEmpty()) {
            return lawyerRepository.findByLawyerDegreeIn(dto.getDegrees(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getCreatedAt() != null || dto.getUpdatedAt() != null) {
            return lawyerRepository.findByCreatedAtAndUpdatedAt(dto.getCreatedAt(), dto.getUpdatedAt(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getStartDate() != null || dto.getEndDate() != null) {
            return lawyerRepository.findByDateRange(dto.getStartDate(), dto.getEndDate(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getVisitScheduleLocation() != null || dto.getVisitScheduleRemarks() != null ||
                dto.getVisitScheduleDate() != null || dto.getVisitScheduleTime() != null) {
            return lawyerRepository.findByVisitSchedule(
                            dto.getVisitScheduleLocation(), dto.getVisitScheduleRemarks(),
                            dto.getVisitScheduleDate(), dto.getVisitScheduleTime(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getName() != null && !dto.getName().isBlank()) {
            return lawyerRepository.findByName(dto.getName(), pageable)
                    .map(this::mapToDTO);
        }
        return lawyerRepository.findByIsActiveTrue(pageable).map(this::mapToDTO);
    }

    private Lawyer mapToEntity(CreateLawyerDTO dto) {
        return Lawyer.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getFirstName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .prisoners(fetchPrisoners(dto.getPrisonerIds()))
                .phoneNumber(dto.getPhoneNumber())
                .company(dto.getCompany())
                .degree(dto.getDegree())
                .nationalId(dto.getNationalId())
                .visitStatus(dto.getVisitStatus() != null ? dto.getVisitStatus() : VisitStatus.PENDING)
                .visitSchedule(dto.getVisitSchedule())
                .role(dto.getRole())
                .isActive(true)
                .build();
    }

    private LawyerDTO mapToDTO(Lawyer lawyer) {
        return LawyerDTO.builder()
                .id(lawyer.getId())
                .firstName(lawyer.getFirstName())
                .lastName(lawyer.getLastName())
                .email(lawyer.getEmail())
                .prisonerIds(lawyer.getPrisoners().stream().map(Prisoner::getId).collect(Collectors.toSet()))
                .phoneNumber(lawyer.getPhoneNumber())
                .company(lawyer.getCompany())
                .degree(lawyer.getDegree())
                .nationalId(lawyer.getNationalId())
                .visitStatus(lawyer.getVisitStatus())
                .visitSchedule(lawyer.getVisitSchedule())
                .role(lawyer.getRole())
                .createdAt(lawyer.getCreatedAt())
                .updatedAt(lawyer.getUpdatedAt())
                .isActive(lawyer.isActive())
                .build();
    }

    private Set<Prisoner> fetchPrisoners(Set<UUID> prisonerIds) {
        if (prisonerIds == null || prisonerIds.isEmpty()) {
            return Set.of();
        }
        return prisonerIds.stream()
                .map(id -> prisonerRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Prisoner not found with ID: " + id)))
                .collect(Collectors.toSet());
    }
}
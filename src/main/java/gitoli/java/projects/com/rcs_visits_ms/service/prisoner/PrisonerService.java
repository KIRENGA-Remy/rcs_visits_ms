package gitoli.java.projects.com.rcs_visits_ms.service.prisoner;

import gitoli.java.projects.com.rcs_visits_ms.dto.prisoner.CreatePrisonerDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.prisoner.PrisonerDTO;
import gitoli.java.projects.com.rcs_visits_ms.entity.lawyer.Lawyer;
import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
import gitoli.java.projects.com.rcs_visits_ms.entity.visitor.Visitor;
import gitoli.java.projects.com.rcs_visits_ms.repository.lawyer.LawyerRepository;
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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrisonerService {
    private final PrisonerRepository prisonerRepository;
    private final LawyerRepository lawyerRepository;
    private final VisitorRepository visitorRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PrisonerDTO createPrisoner(CreatePrisonerDTO dto) {
        if (prisonerRepository.findByNationalId(dto.getNationalId()).isPresent()) {
            throw new IllegalArgumentException("National ID already exists");
        }

        if (prisonerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Prisoner prisoner = mapToEntity(dto);
        prisoner.setPassword(passwordEncoder.encode(dto.getPassword()));
        prisoner = prisonerRepository.save(prisoner);
        return mapToDTO(prisoner);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PrisonerDTO updatePrisoner(UUID id, CreatePrisonerDTO dto) {
        Prisoner prisoner = prisonerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prisoner not found with ID: " + id));

        prisonerRepository.findByNationalId(dto.getNationalId())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> {
                    throw new IllegalArgumentException("National ID already exists");
                });
        prisonerRepository.findByEmail(dto.getEmail())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> {
                    throw new IllegalArgumentException("Email already exists");
                });

        prisoner.setFirstName(dto.getFirstName());
        prisoner.setLastName(dto.getLastName());
        prisoner.setGender(dto.getGender());
        prisoner.setDateOfBirth(dto.getDateOfBirth());
        prisoner.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            prisoner.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        prisoner.setLawyers(fetchLawyers(dto.getLawyerIds()));
        prisoner.setVisitors(fetchVisitors(dto.getVisitorIds()));
        prisoner.setDateOfImprisonment(dto.getDateOfImprisonment());
        prisoner.setDateOfRelease(dto.getDateOfRelease());
        prisoner.setStatus(dto.getStatus());
        prisoner.setCourtStatus(dto.getCourtStatus());
        prisoner.setCourtReport(dto.getCourtReport());
        prisoner.setConvictionOfCrime(dto.getConvictionOfCrime());
        prisoner.setNationalId(dto.getNationalId());
        prisoner.setNationality(dto.getNationality());
        prisoner.setHealthDetails(dto.getHealthDetails());
        prisoner.setRole(dto.getRole());

        prisoner = prisonerRepository.save(prisoner);
        return mapToDTO(prisoner);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PrisonerDTO getPrisoner(UUID id) {
        Prisoner prisoner = prisonerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prisoner not found with ID: " + id));
        return mapToDTO(prisoner);
        @PreAuthorize("hasRole('ADMIN')")
        public PrisonerDTO getPrisoner (UUID id){
            Prisoner prisoner = prisonerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Prisoner not found with ID: " + id));
            return mapToDTO(prisoner);
        }

        @PreAuthorize("hasRole('ADMIN')")
        public void deletePrisoner (UUID id){
            if (!prisonerRepository.existsById(id)) {
                throw new IllegalArgumentException("Prisoner not found with ID: " + id);
            }
            prisonerRepository.deleteById(id);
        }

        @PreAuthorize("hasRole('ADMIN')")
        public Page<PrisonerDTO> searchPrisoners(SearchPrisonerDTO dto, Pageable pageable){
            return prisonerRepository.searchPrisoners(
                    dto.getStatus(),
                    dto.getCourtStatus(),
                    dto.getNationality(),
                    dto.getName(),
                    pageable
            ).map(this::mapToDTO);
        }
        private Prisoner mapToEntity (CreatePrisonerDTO dto){
            return Prisoner.builder()
                    .firstName(dto.getFirstName())
                    .lastName(dto.getLastName())
                    .gender(dto.getGender())
                    .dateOfBirth(dto.getDateOfBirth())
                    .email(dto.getEmail())
                    .password(dto.getPassword())
                    .lawyers(fetchLawyers(dto.getLawyerIds()))
                    .visitors(fetchVisitors(dto.getVisitorIds()))
                    .dateOfImprisonment(dto.getDateOfImprisonment())
                    .dateOfRelease(dto.getDateOfRelease())
                    .status(dto.getStatus())
                    .courtStatus(dto.getCourtStatus())
                    .courtReport(dto.getCourtReport())
                    .convictionOfCrime(dto.getConvictionOfCrime())
                    .nationalId(dto.getNationalId())
                    .nationality(dto.getNationality())
                    .healthDetails(dto.getHealthDetails())
                    .role(dto.getRole())
                    .isActive(true)
                    .build();
        }

        private PrisonerDTO mapToDTO (Prisoner prisoner){
            return PrisonerDTO.builder()
                    .id(prisoner.getId())
                    .firstName(prisoner.getFirstName())
                    .lastName(prisoner.getLastName())
                    .gender(prisoner.getGender())
                    .dateOfBirth(prisoner.getDateOfBirth())
                    .lawyerIds(prisoner.getLawyers().stream().map(Lawyer::getId).collect(Collectors.toSet()))
                    .visitorIds(prisoner.getVisitors().stream().map(Visitor::getId).collect(Collectors.toSet()))
                    .dateOfImprisonment(prisoner.getDateOfImprisonment())
                    .dateOfRelease(prisoner.getDateOfRelease())
                    .status(prisoner.getStatus())
                    .courtStatus(prisoner.getCourtStatus())
                    .courtReport(prisoner.getCourtReport())
                    .convictionOfCrime(prisoner.getConvictionOfCrime())
                    .nationalId(prisoner.getNationalId())
                    .nationality(prisoner.getNationality())
                    .healthDetails(prisoner.getHealthDetails())
                    .role(prisoner.getRole())
                    .createdAt(prisoner.getCreatedAt())
                    .updatedAt(prisoner.getUpdatedAt())
                    .isActive(prisoner.isActive())
                    .build();
        }

        private Set<Lawyer> fetchLawyers (Set < UUID > lawyerIds) {
            if (lawyerIds == null) return Set.of();
            return lawyerIds.stream()
                    .map(id -> lawyerRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Lawyer not found with ID: " + id)))
                    .collect(Collectors.toSet());
        }

        private Set<Visitor> fetchVisitors (Set < UUID > visitorIds) {
            if (visitorIds == null) return Set.of();
            return visitorIds.stream()
                    .map(id -> visitorRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Visitor not found with ID: " + id)))
                    .collect(Collectors.toSet());
        }
    }
}

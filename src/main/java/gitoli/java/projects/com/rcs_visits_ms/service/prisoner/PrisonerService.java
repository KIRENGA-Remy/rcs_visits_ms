package gitoli.java.projects.com.rcs_visits_ms.service.prisoner;

import gitoli.java.projects.com.rcs_visits_ms.dto.prisoner.CreatePrisonerDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.prisoner.PrisonerDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.prisoner.PrisonerMapper;
import gitoli.java.projects.com.rcs_visits_ms.dto.prisoner.SearchPrisonerDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.visitor.VisitorDTO;
import gitoli.java.projects.com.rcs_visits_ms.entity.lawyer.Lawyer;
import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
import gitoli.java.projects.com.rcs_visits_ms.entity.visitor.Visitor;
import gitoli.java.projects.com.rcs_visits_ms.repository.lawyer.LawyerRepository;
import gitoli.java.projects.com.rcs_visits_ms.repository.prisoner.PrisonerRepository;
import gitoli.java.projects.com.rcs_visits_ms.repository.visitor.VisitorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrisonerService {
    private final PrisonerRepository prisonerRepository;
    private LawyerRepository lawyerRepository;
    private VisitorRepository visitorRepository;
    private PasswordEncoder passwordEncoder;
    private PrisonerMapper prisonerMapper;

    public String generateNextPrisonerCode(){
        List<String> allCodes = prisonerRepository.findAllPrisonerCodes();
        Set<Integer> codeNumbers = allCodes.stream()
                .filter(code -> code != null && code.startsWith("code"))
                .map(code -> {
                    try{
                        return Integer.parseInt(code.substring(4));
                    } catch (NumberFormatException e){
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        int i = 1;
        while (codeNumbers.contains(i)){
            i++;
        }
        return "code"+i;
    }

    private Set<Lawyer> fetchLawyers(Set<UUID> lawyerIds){
        if (lawyerIds == null) return Set.of();
        return lawyerIds.stream()
                .map(id -> lawyerRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Lawyer not found with ID: "+ id)))
                .collect(Collectors.toSet());
    }

    private Set<Visitor> fetchVisitors(Set<UUID> visitorIds){
        if(visitorIds == null) return Set.of();
        return visitorIds.stream()
                .map(id -> visitorRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Visitor not found with this ID: "+ id)))
                .collect(Collectors.toSet());
    }

    private Prisoner mapToEntity(CreatePrisonerDTO dto){
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
                .courtReport(dto.getCourtReport())
                .convictionOfCrime(dto.getConvictionOfCrime())
                .nationality(dto.getNationality())
                .nationalId(dto.getNationalId())
                .healthDetails(dto.getHealthDetails())
                .role(dto.getRole())
                .isActive(true)
                .build();
    }

    @Transactional
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public PrisonerDTO createPrisoner(CreatePrisonerDTO dto){
        if (prisonerRepository.findByNationalId(dto.getNationalId()).isPresent()){
            throw new IllegalArgumentException("National id already exists");
        }

        String nextCode = generateNextPrisonerCode();
        Prisoner prisoner = mapToEntity(dto);
        prisoner.setPrisonerCode(nextCode);
        prisoner.setPassword(passwordEncoder.encode(dto.getPassword()));

        prisonerRepository.save(prisoner);
        return prisonerMapper.toDTO(prisoner);
    }

    @Transactional
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public PrisonerDTO updatePrisoner(UUID id, CreatePrisonerDTO dto){
        Prisoner prisoner = prisonerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prisoner not found with this ID: "+ id));

        prisonerRepository.findByNationalId(dto.getNationalId())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> { throw new IllegalArgumentException("National Id already exists"); });

        prisoner.setFirstName(dto.getFirstName());
        prisoner.setLastName(dto.getLastName());
        prisoner.setGender(dto.getGender());
        prisoner.setDateOfBirth(dto.getDateOfBirth());
        prisoner.setEmail(dto.getEmail());
        if(dto.getPassword() != null && dto.getPassword().isBlank()){
            prisoner.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        prisoner.setLawyers(fetchLawyers(dto.getLawyerIds()));
        prisoner.setVisitors(fetchVisitors(dto.getVisitorIds()));
        prisoner.setDateOfImprisonment(dto.getDateOfImprisonment());
        prisoner.setDateOfRelease(dto.getDateOfRelease());
        prisoner.setStatus(dto.getStatus());
        prisoner.setCourtReport(dto.getCourtReport());
        prisoner.setCourtStatus(dto.getCourtStatus());
        prisoner.setConvictionOfCrime(dto.getConvictionOfCrime());
        prisoner.setNationalId(dto.getNationalId());
        prisoner.setNationality(dto.getNationality());
        prisoner.setHealthDetails(dto.getHealthDetails());
        prisoner.setRole(dto.getRole());
        prisoner = prisonerRepository.save(prisoner);

        return prisonerMapper.toDTO(prisoner);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public PrisonerDTO getPrisoner( UUID id){
        Prisoner prisoner = prisonerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prisoner not found with this ID: "+ id));
        return prisonerMapper.toDTO(prisoner);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public Page<PrisonerDTO> getPrisoners(Pageable pageable){
         return prisonerRepository.findAll(
                 pageable
         ).map(prisonerMapper::toDTO);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public void deletePrisoner(UUID id){
        if(!prisonerRepository.existsById(id)){
            throw new IllegalArgumentException("Prisoner not found with this ID: "+ id);
        }
        prisonerRepository.deleteById(id);
    }

    public Page<PrisonerDTO> searchPrisoners(SearchPrisonerDTO dto, Pageable pageable){
        return prisonerRepository.searchPrisoners(
                dto.getStatus(),
                dto.getCourtStatus(),
                dto.getNationality(),
                dto.getName(),
                pageable
        ).map(prisonerMapper::toDTO);
    }

    @Transactional
    public void addVisitorToPrisoner(UUID prisonerId, VisitorDTO dto){
        Prisoner prisoner = prisonerRepository.findById(prisonerId)
                .orElseThrow(() -> new IllegalArgumentException("Prisoner not found with this ID: "+ prisonerId));

        Visitor visitor = new Visitor();
        visitor.setFirstName(dto.getFirstName());
        visitor.setLastName(dto.getLastName());
        visitor.setEmail(dto.getEmail());
        visitor.setPhoneNumber(dto.getPhoneNumber());
        visitor.setRelationship(dto.getRelationship());
        visitor.setPrisoner(prisoner);

        visitorRepository.save(visitor);
    }
}
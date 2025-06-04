package gitoli.java.projects.com.rcs_visits_ms.role_visitor.service;

import gitoli.java.projects.com.rcs_visits_ms.role_prisoner.entity.Prisoner;
import gitoli.java.projects.com.rcs_visits_ms.role_prisoner.repository.PrisonerRepository;
import gitoli.java.projects.com.rcs_visits_ms.role_visitor.dto.VisitorDTO;
import gitoli.java.projects.com.rcs_visits_ms.role_visitor.dto.VisitorRequest;
import gitoli.java.projects.com.rcs_visits_ms.role_visitor.dto.VisitorResponse;
import gitoli.java.projects.com.rcs_visits_ms.role_visitor.entity.Visitor;
import gitoli.java.projects.com.rcs_visits_ms.role_visitor.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public VisitorResponse createVisitor(VisitorRequest request) {
        if (visitorRepository.existsByEmail(request.getEmail())) {
            return new VisitorResponse(null, null, null, null, null, null, null, null,
                    "Email already exists");
        }
        if (visitorRepository.existsByNationalId(request.getNationalId())) {
            return new VisitorResponse(null, null, null, null, null, null, null, null,
                    "National ID already exists");
        }

        Prisoner prisoner = prisonerRepository.findById(request.getPrisonerId())
                .orElseThrow(() -> new RuntimeException("Prisoner not found"));

        Visitor visitor = new Visitor();
        visitor.setFirstName(request.getFirstName());
        visitor.setLastName(request.getLastName());
        visitor.setEmail(request.getEmail());
        visitor.setPassword(request.getPassword()); // Consider password encoding
        visitor.setRelationship(request.getRelationship());
        visitor.setNumberOfVisitors(request.getNumberOfVisitors());
        visitor.setPrisoner(prisoner);
        visitor.setPhoneNumber(request.getPhoneNumber());
        visitor.setNationalId(request.getNationalId());

        Visitor savedVisitor = visitorRepository.save(visitor);
        return mapToResponse(savedVisitor, "Visitor created successfully");
    }

    @Transactional(readOnly = true)
    public VisitorResponse getVisitorById(UUID id) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visitor not found"));
        return mapToResponse(visitor, "Visitor retrieved successfully");
    }

    @Transactional(readOnly = true)
    public List<VisitorResponse> getAllVisitorsByPrisonerId(UUID prisonerId) {
        return visitorRepository.findAllByPrisonerId(prisonerId)
                .stream()
                .map(visitor -> mapToResponse(visitor, "Visitor retrieved successfully"))
                .collect(Collectors.toList());
    }

    @Transactional
    public VisitorResponse updateVisitor(UUID id, VisitorRequest request) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visitor not found"));

        if (!visitor.getEmail().equals(request.getEmail()) &&
                visitorRepository.existsByEmail(request.getEmail())) {
            return new VisitorResponse(null, null, null, null, null, null, null, null,
                    "Email already exists");
        }
        if (!visitor.getNationalId().equals(request.getNationalId()) &&
                visitorRepository.existsByNationalId(request.getNationalId())) {
            return new VisitorResponse(null, null, null, null, null, null, null, null,
                    "National ID already exists");
        }

        Prisoner prisoner = prisonerRepository.findById(request.getPrisonerId())
                .orElseThrow(() -> new RuntimeException("Prisoner not found"));

        visitor.setFirstName(request.getFirstName());
        visitor.setLastName(request.getLastName());
        visitor.setEmail(request.getEmail());
        visitor.setPassword(request.getPassword());
        visitor.setRelationship(request.getRelationship());
        visitor.setNumberOfVisitors(request.getNumberOfVisitors());
        visitor.setPrisoner(prisoner);
        visitor.setPhoneNumber(request.getPhoneNumber());
        visitor.setNationalId(request.getNationalId());

        Visitor updatedVisitor = visitorRepository.save(visitor);
        return mapToResponse(updatedVisitor, "Visitor updated successfully");
    }

    @Transactional
    public void deleteVisitor(UUID id) {
        if (!visitorRepository.existsById(id)) {
            throw new RuntimeException("Visitor not found");
        }
        visitorRepository.deleteById(id);
    }

    private VisitorResponse mapToResponse(Visitor visitor, String message) {
        return new VisitorResponse(
                visitor.getId(),
                visitor.getFirstName(),
                visitor.getLastName(),
                visitor.getEmail(),
                visitor.getRelationship(),
                visitor.getNumberOfVisitors(),
                visitor.getPrisoner().getId(),
                visitor.getPhoneNumber(),
                visitor.getNationalId(),
                message
        );
    }
}
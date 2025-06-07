package gitoli.java.projects.com.rcs_visits_ms.service.visitor;
import com.gitoli.rcs_visits_ms.dto.VisitorRequest;
import com.gitoli.rcs_visits_ms.dto.VisitorResponse;
import com.gitoli.rcs_visits_ms.entity.Prisoner;
import com.gitoli.rcs_visits_ms.entity.Visitor;
import com.gitoli.rcs_visits_ms.exception.ResourceAlreadyExistsException;
import com.gitoli.rcs_visits_ms.exception.ResourceNotFoundException;
import com.gitoli.rcs_visits_ms.repository.PrisonerRepository;
import com.gitoli.rcs_visits_ms.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private static final Logger logger = LoggerFactory.getLogger(VisitorService.class);

    private final VisitorRepository visitorRepository;
    private final PrisonerRepository prisonerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public VisitorResponse createVisitor(VisitorRequest request) {
        logger.info("Attempting to create visitor with email: {}", request.getEmail());

        if (visitorRepository.existsByEmail(request.getEmail())) {
            logger.warn("Email {} already exists", request.getEmail());
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        if (visitorRepository.existsByNationalId(request.getNationalId())) {
            logger.warn("National ID {} already exists", request.getNationalId());
            throw new ResourceAlreadyExistsException("National ID already exists");
        }

        Prisoner prisoner = prisonerRepository.findById(request.getPrisonerId())
                .orElseThrow(() -> {
                    logger.error("Prisoner with ID {} not found", request.getPrisonerId());
                    return new ResourceNotFoundException("Prisoner not found");
                });

        Visitor visitor = new Visitor();
        visitor.setFirstName(request.getFirstName());
        visitor.setLastName(request.getLastName());
        visitor.setEmail(request.getEmail());
        visitor.setPassword(passwordEncoder.encode(request.getPassword()));
        visitor.setRelationship(request.getRelationship());
        visitor.setNumberOfVisitors(request.getNumberOfVisitors());
        visitor.setPrisoner(prisoner);
        visitor.setPhoneNumber(request.getPhoneNumber());
        visitor.setNationalId(request.getNationalId());

        Visitor savedVisitor = visitorRepository.save(visitor);
        logger.info("Visitor created successfully with ID: {}", savedVisitor.getId());
        return mapToResponse(savedVisitor, "Visitor created successfully");
    }

    @Transactional(readOnly = true)
    public VisitorResponse getVisitorById(UUID id) {
        logger.info("Retrieving visitor with ID: {}", id);
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Visitor with ID {} not found", id);
                    return new ResourceNotFoundException("Visitor not found");
                });
        return mapToResponse(visitor, "Visitor retrieved successfully");
    }

    @Transactional(readOnly = true)
    public List<VisitorResponse> getAllVisitorsByPrisonerId(UUID prisonerId) {
        logger.info("Retrieving visitors for prisoner ID: {}", prisonerId);
        return visitorRepository.findAllByPrisonerId(prisonerId)
                .stream()
                .map(visitor -> mapToResponse(visitor, "Visitor retrieved successfully"))
                .collect(Collectors.toList());
    }

    @Transactional
    public VisitorResponse updateVisitor(UUID id, VisitorRequest request) {
        logger.info("Updating visitor with ID: {}", id);
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Visitor with ID {} not found", id);
                    return new ResourceNotFoundException("Visitor not found");
                });

        if (!visitor.getEmail().equals(request.getEmail()) &&
                visitorRepository.existsByEmail(request.getEmail())) {
            logger.warn("Email {} already exists", request.getEmail());
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        if (!visitor.getNationalId().equals(request.getNationalId()) &&
                visitorRepository.existsByNationalId(request.getNationalId())) {
            logger.warn("National ID {} already exists", request.getNationalId());
            throw new ResourceAlreadyExistsException("National ID already exists");
        }

        Prisoner prisoner = prisonerRepository.findById(request.getPrisonerId())
                .orElseThrow(() -> {
                    logger.error("Prisoner with ID {} not found", request.getPrisonerId());
                    return new ResourceNotFoundException("Prisoner not found");
                });

        visitor.setFirstName(request.getFirstName());
        visitor.setLastName(request.getLastName());
        visitor.setEmail(request.getEmail());
        visitor.setPassword(passwordEncoder.encode(request.getPassword()));
        visitor.setRelationship(request.getRelationship());
        visitor.setNumberOfVisitors(request.getNumberOfVisitors());
        visitor.setPrisoner(prisoner);
        visitor.setPhoneNumber(request.getPhoneNumber());
        visitor.setNationalId(request.getNationalId());

        Visitor updatedVisitor = visitorRepository.save(visitor);
        logger.info("Visitor updated successfully with ID: {}", updatedVisitor.getId());
        return mapToResponse(updatedVisitor, "Visitor updated successfully");
    }

    @Transactional
    public void deleteVisitor(UUID id) {
        logger.info("Deleting visitor with ID: {}", id);
        if (!visitorRepository.existsById(id)) {
            logger.error("Visitor with ID {} not found", id);
            throw new ResourceNotFoundException("Visitor not found");
        }
        visitorRepository.deleteById(id);
        logger.info("Visitor deleted successfully with ID: {}", id);
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
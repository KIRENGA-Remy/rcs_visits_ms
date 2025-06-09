package gitoli.java.projects.com.rcs_visits_ms.service.prisoner;

import gitoli.java.projects.com.rcs_visits_ms.repository.lawyer.LawyerRepository;
import gitoli.java.projects.com.rcs_visits_ms.repository.prisoner.PrisonerRepository;
import gitoli.java.projects.com.rcs_visits_ms.repository.visitor.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrisonerService {
    private final PrisonerRepository prisonerRepository;
    private final VisitorRepository visitorRepository;
    private final LawyerRepository lawyerRepository;
}

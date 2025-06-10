package gitoli.java.projects.com.rcs_visits_ms.dto.prisoner;

import gitoli.java.projects.com.rcs_visits_ms.entity.lawyer.Lawyer;
import gitoli.java.projects.com.rcs_visits_ms.entity.prisoner.Prisoner;
import gitoli.java.projects.com.rcs_visits_ms.entity.visitor.Visitor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PrisonerMapper {

    public PrisonerDTO toDTO(Prisoner prisoner) {
        return PrisonerDTO.builder()
                .id(prisoner.getId())
                .prisonerCode(prisoner.getPrisonerCode())
                .nationalId(prisoner.getNationalId())
                .firstName(prisoner.getFirstName())
                .lastName(prisoner.getLastName())
                .gender(prisoner.getGender())
                .status(prisoner.getStatus())
                .nationality(prisoner.getNationality())
                .dateOfImprisonment(prisoner.getDateOfImprisonment())
                .dateOfRelease(prisoner.getDateOfRelease())
                .dateOfBirth(prisoner.getDateOfBirth())
                .convictionOfCrime(prisoner.getConvictionOfCrime())
                .healthDetails(prisoner.getHealthDetails())
                .courtReport(prisoner.getCourtReport())
                .visitorIds(
                        prisoner.getVisitors() == null ? Set.of() :
                                prisoner.getVisitors().stream().map(Visitor::getId).collect(Collectors.toSet())
                )
                .lawyerIds(
                        prisoner.getLawyers() == null ? Set.of() :
                                prisoner.getLawyers().stream().map(Lawyer::getId).collect(Collectors.toSet())
                )
                .build();
    }
}

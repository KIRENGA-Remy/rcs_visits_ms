package gitoli.java.projects.com.rcs_visits_ms.role_visitor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitorResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String relationship;
    private Integer numberOfVisitors;
    private UUID prisonerId;
    private String phoneNumber;
    private String nationalId;
    private String message;
}
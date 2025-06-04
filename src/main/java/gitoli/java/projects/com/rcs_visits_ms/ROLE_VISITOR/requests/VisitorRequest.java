package gitoli.java.projects.com.rcs_visits_ms.ROLE_VISITOR.requests;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

public class VisitorRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String relationship;
    private Integer numberOfVisitors;
    private String prisoner;
    private String phoneNumber;
    private String nationalId;
}

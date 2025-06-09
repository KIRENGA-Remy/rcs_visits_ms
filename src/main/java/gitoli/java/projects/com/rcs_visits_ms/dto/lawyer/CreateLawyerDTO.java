package gitoli.java.projects.com.rcs_visits_ms.dto.lawyer;

import gitoli.java.projects.com.rcs_visits_ms.entity.VisitSchedule;
import gitoli.java.projects.com.rcs_visits_ms.enums.LawyerDegree;
import gitoli.java.projects.com.rcs_visits_ms.enums.Role;
import gitoli.java.projects.com.rcs_visits_ms.enums.VisitStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLawyerDTO {
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 70, message = "Email must be at most 70 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 70, message = "Password must be between 8 and 70 characters")
    private String password;

    private Set<UUID> prisonerIds;

    @NotBlank(message = "Phone number is required")
    @Size(max = 15, message = "Phone number must be at most 15 characters")
    private String phoneNumber;

    @NotBlank(message = "Company is required")
    @Size(max = 100, message = "Company must be at most 100 characters")
    private String company;

    @NotNull(message = "Degree is required")
    private LawyerDegree degree;

    @NotBlank(message = "National ID is required")
    @Size(max = 70, message = "National ID must be at most 70 characters")
    private String nationalId;

    private VisitStatus visitStatus;

    private VisitSchedule visitSchedule;

    @NotNull(message = "Role is required")
    private Role role;
}
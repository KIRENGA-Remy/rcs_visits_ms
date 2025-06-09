package gitoli.java.projects.com.rcs_visits_ms.dto.admin;

import gitoli.java.projects.com.rcs_visits_ms.enums.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String officeName;
    private Role role;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private boolean isActive;
}
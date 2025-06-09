package gitoli.java.projects.com.rcs_visits_ms.dto.admin;

import gitoli.java.projects.com.rcs_visits_ms.enums.Role;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAdminDTO {
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

    @NotBlank(message = "Phone number is required")
    @Size(max = 15, message = "Phone number must be at most 15 characters")
    private String phoneNumber;

    @NotBlank(message = "Office name is required")
    @Size(max = 100, message = "Office name must be at most 100 characters")
    private String officeName;

    @NotNull(message = "Signature is required")
    private byte[] signature;

    @NotNull(message = "Stamp is required")
    private byte[] stamp;

    @NotNull(message = "Role is required")
    private Role role;
}
package gitoli.java.projects.com.rcs_visits_ms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitScheduleDTO {

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Visit date is required")
    private LocalDate visitDateTime;

    @NotBlank(message = "Visit time is required")
    private String visitTime;

    private String remarks;
}

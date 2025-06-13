package gitoli.java.projects.com.rcs_visits_ms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate visitDate;

    @NotBlank(message = "Visit time is required")
    private String visitTime;

    private String remarks;
}

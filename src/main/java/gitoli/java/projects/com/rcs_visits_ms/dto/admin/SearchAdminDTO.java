package gitoli.java.projects.com.rcs_visits_ms.dto.admin;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchAdminDTO {
    private String name;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private String officeName;
}
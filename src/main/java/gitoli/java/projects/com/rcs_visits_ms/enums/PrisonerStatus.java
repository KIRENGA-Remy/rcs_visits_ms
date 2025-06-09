package gitoli.java.projects.com.rcs_visits_ms.enums;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;

public enum PrisonerStatus {
    DETAINED,
    SENTENCED,
    RELEASED,
    LIFE_IMPRISONMENT
}

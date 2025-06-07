package gitoli.java.projects.com.rcs_visits_ms.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NationalIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NationalId {
    String message() default "Invalid national ID format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

class NationalIdValidator implements ConstraintValidator<NationalId, String> {
    private static final String NATIONAL_ID_REGEX = "^[A-Z0-9]{8,20}$"; // Example regex, adjust as needed

    @Override
    public void initialize(NationalId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String nationalId, ConstraintValidatorContext context) {
        if (nationalId == null) {
            return false;
        }
        return nationalId.matches(NATIONAL_ID_REGEX);
    }
}
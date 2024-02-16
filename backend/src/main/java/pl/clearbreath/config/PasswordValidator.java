package pl.clearbreath.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        boolean hasUpperCase = !password.equals(password.toLowerCase());
        boolean hasLowerCase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");

        boolean isValid = true;

        if (!hasUpperCase) {
            isValid = false;
            context.buildConstraintViolationWithTemplate("Password must contain at least one uppercase letter.")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        if (!hasLowerCase) {
            isValid = false;
            context.buildConstraintViolationWithTemplate("Password must contain at least one lowercase letter.")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        if (!hasDigit) {
            isValid = false;
            context.buildConstraintViolationWithTemplate("Password must contain at least one digit.")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return isValid;
    }
}



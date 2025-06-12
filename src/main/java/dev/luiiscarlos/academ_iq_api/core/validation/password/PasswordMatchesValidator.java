package dev.luiiscarlos.academ_iq_api.core.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordConfirmable> {

    @Override
    public boolean isValid(PasswordConfirmable obj, ConstraintValidatorContext context) {
        if (obj.getPassword() == null || !obj.getPassword().equals(obj.getConfirmPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
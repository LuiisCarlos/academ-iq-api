package dev.luiiscarlos.academ_iq_api.shared.validation;

import dev.luiiscarlos.academ_iq_api.auth.dto.AuthRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, AuthRequest> {

    @Override
    public boolean isValid(AuthRequest dto, ConstraintValidatorContext context) {
        return dto.getPassword() != null &&
               dto.getPassword().equals(dto.getConfirmPassword());
    }
}
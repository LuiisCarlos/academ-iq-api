package dev.luiiscarlos.academ_iq_api.utilities.annotations;

import dev.luiiscarlos.academ_iq_api.models.dtos.user.RegisterRequestDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterRequestDto> {

    @Override
    public boolean isValid(RegisterRequestDto dto, ConstraintValidatorContext context) {
        return dto.getPassword() != null &&
               dto.getPassword().equals(dto.getConfirmPassword());
    }
}
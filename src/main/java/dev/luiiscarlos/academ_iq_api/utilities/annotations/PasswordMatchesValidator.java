package dev.luiiscarlos.academ_iq_api.utilities.annotations;

import dev.luiiscarlos.academ_iq_api.models.dtos.user.UserRegisterRequestDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserRegisterRequestDto> {

    @Override
    public boolean isValid(UserRegisterRequestDto dto, ConstraintValidatorContext context) {
        return dto.getPassword() != null &&
               dto.getPassword().equals(dto.getConfirmPassword());
    }
}
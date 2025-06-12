package dev.luiiscarlos.academ_iq_api.core.validation.adult;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        if (birthdate == null)
            return false;

        return Period.between(birthdate, LocalDate.now()).getYears() >= 18;
    }
}

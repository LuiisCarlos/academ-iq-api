package dev.luiiscarlos.academ_iq_api.features.auth.dto;

import java.time.LocalDate;

import dev.luiiscarlos.academ_iq_api.core.validation.Adult;
import dev.luiiscarlos.academ_iq_api.core.validation.PasswordMatches;
import lombok.Data;

@Data
@PasswordMatches
public class AuthRequest {

    private String username;

    private String password;

    private String confirmPassword;

    private String email;

    private String firstname;

    private String lastname;

    private String phone;

    @Adult
    private LocalDate birthdate;

}

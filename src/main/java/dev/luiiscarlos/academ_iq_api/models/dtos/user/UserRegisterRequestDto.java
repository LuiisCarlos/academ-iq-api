package dev.luiiscarlos.academ_iq_api.models.dtos.user;

import java.time.LocalDate;

import dev.luiiscarlos.academ_iq_api.utilities.annotations.Adult;
import dev.luiiscarlos.academ_iq_api.utilities.annotations.PasswordMatches;

import lombok.Data;

@Data
@PasswordMatches
public class UserRegisterRequestDto {

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

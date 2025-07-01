package dev.luiiscarlos.academ_iq_api.features.identity.auth.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private String username;

    private String email;

    private String firstname;

    private String lastname;

    private String fullname;

    private String phone;

    @JsonFormat(shape = Shape.STRING)
    private LocalDate birthdate;

}

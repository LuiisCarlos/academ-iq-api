package dev.luiiscarlos.academ_iq_api.models.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterResponseDto {

    private String username;

    private String email;

    private String firstname;

    private String lastname;

    private String phone;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthdate;

}

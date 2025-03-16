package dev.luiiscarlos.academ_iq_api.services.interfaces;

import dev.luiiscarlos.academ_iq_api.models.dtos.UserResetPasswordDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserLoginRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserLoginResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserRegisterResponseDto;;

public interface AuthService {

    UserLoginResponseDto login(String origin, UserLoginRequestDto loginRequest);

    UserRegisterResponseDto register(String origin, UserRegisterRequestDto registerRequest);

    String refresh(String tokenJson);

    void logout(String token);

    void verify(String token);

    void recoverPassword(String origin, String email);

    void resetPassword(String token, UserResetPasswordDto resetPassword);

}

package dev.luiiscarlos.academ_iq_api.services.interfaces;

import dev.luiiscarlos.academ_iq_api.models.dtos.user.PasswordResetDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.UserLoginRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.UserLoginResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.UserRegisterResponseDto;;

public interface AuthService {

    UserLoginResponseDto login(String origin, UserLoginRequestDto userDto);

    UserRegisterResponseDto register(String origin, UserRegisterRequestDto userDto);

    String refresh(String token);

    void logout(String token);

    void verify(String token);

    void recoverPassword(String origin, String email);

    void resetPassword(String token, PasswordResetDto passwordDto);

}

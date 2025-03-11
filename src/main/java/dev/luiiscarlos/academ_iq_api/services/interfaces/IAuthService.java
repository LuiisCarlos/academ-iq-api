package dev.luiiscarlos.academ_iq_api.services.interfaces;

import dev.luiiscarlos.academ_iq_api.models.dtos.UserChangePasswordDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserLoginRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserLoginResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserRegisterResponseDto;

public interface IAuthService {

    UserLoginResponseDto login(UserLoginRequestDto loginRequest);

    UserRegisterResponseDto register(UserRegisterRequestDto registerRequest);

    String refresh(String tokenJson);

    void logout(String token);

    String recoverPassword(String email);

    void changePassword(String token, UserChangePasswordDto changePassword);

}

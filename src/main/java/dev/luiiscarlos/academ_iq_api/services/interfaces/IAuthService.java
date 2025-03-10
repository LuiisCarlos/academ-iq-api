package dev.luiiscarlos.academ_iq_api.services.interfaces;

import dev.luiiscarlos.academ_iq_api.dtos.UserLoginRequestDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserLoginResponseDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserRegisterResponseDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserChangePasswordDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {

    UserLoginResponseDto login(UserLoginRequestDto loginRequest);

    UserRegisterResponseDto register(UserRegisterRequestDto registerRequest);

    String refresh(String tokenJson);

    void logout(HttpServletRequest request, HttpServletResponse response, String token);

    String recoverPassword(String email);

    void changePassword(UserChangePasswordDto changePassword);

}

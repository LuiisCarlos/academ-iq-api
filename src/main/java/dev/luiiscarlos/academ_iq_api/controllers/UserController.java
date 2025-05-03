package dev.luiiscarlos.academ_iq_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.FileResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.UserUpdateRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.PasswordUpdateDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.UserResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.UserMapper;
import dev.luiiscarlos.academ_iq_api.services.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    @PostMapping("/@me/change-password")
    public ResponseEntity<Void> updatePasswordByToken(
            @RequestHeader("Authorization") String token,
            @RequestBody PasswordUpdateDto passwordDto) {
        userService.updatePasswordByToken(token, passwordDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/@me")
    public ResponseEntity<UserResponseDto> findByToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userMapper.toUserResponseDto(userService.findByToken(token)));
    }

    @PutMapping("/@me")
    public ResponseEntity<UserResponseDto> updateByToken(
            @RequestHeader("Authorization") String token,
            @RequestBody UserUpdateRequestDto userDto) {
        User user = userMapper.toUser(userDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userMapper.toUserResponseDto(userService.updateByToken(token, user)));
    }

    @PutMapping(value = "/@me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponseDto> updateAvatarByToken(
            @RequestHeader("Authorization") String token,
            @RequestPart("avatar") MultipartFile file) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.updateAvatarByToken(token, file));
    }

    @DeleteMapping("/@me")
    public ResponseEntity<Void> deleteByToken(@RequestHeader("Authorization") String token) {
        userService.deleteByToken(token);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/@me/avatar")
    public ResponseEntity<Void> deleteAvatarByToken(@RequestHeader("Authorization") String token) {
        userService.deleteAvatarByToken(token);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}

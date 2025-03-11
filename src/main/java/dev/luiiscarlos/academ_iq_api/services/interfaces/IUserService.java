package dev.luiiscarlos.academ_iq_api.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.FileResponseDto;

public interface IUserService {

    List<User> findAll();


    User findById(Long id);

    User findByUsername(String username);

    FileResponseDto findAvatarById(Long id);

    User save(User user);

    User updateById(Long id, User user);

    FileResponseDto updateAvatarById(Long id, MultipartFile avatar);

    void deleteById(Long id);

    void deleteAvatarById(Long id);


    User findByToken(String token);

    FileResponseDto findAvatarByToken(String token);

    User updateByToken(String token);

    FileResponseDto updateAvatarByToken(String token, MultipartFile avatar);

    void deleteByToken(String token);

    void deleteAvatarByToken(String token);

}

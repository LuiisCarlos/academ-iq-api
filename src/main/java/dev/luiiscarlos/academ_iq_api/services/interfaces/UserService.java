package dev.luiiscarlos.academ_iq_api.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.file.FileResponseDto;

public interface UserService {

    User save(User user);


    List<User> findAll();

    User findById(Long id);

    User findByUsername(String username);

    FileResponseDto findAvatarById(Long id);

    User findByToken(String token);

    FileResponseDto findAvatarByToken(String token);


    User updateById(Long id, User user);

    FileResponseDto patchAvatarById(Long id, MultipartFile avatar);

    User updateByToken(String token, User user);

    FileResponseDto patchAvatarByToken(String token, MultipartFile avatar);


    void deleteById(Long id);

    void deleteAvatarById(Long id);

    void deleteByToken(String token);

    void deleteAvatarByToken(String token);

}

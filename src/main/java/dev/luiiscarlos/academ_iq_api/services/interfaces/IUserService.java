package dev.luiiscarlos.academ_iq_api.services.interfaces;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.models.User;

public interface IUserService {

    List<User> findAll();

    User findById(Long id);

    Resource findAvatarById(Long id);

    User updateById(Long id, User user);

    Resource updateAvatarById(Long id, MultipartFile avatar);

    void deleteById(Long id);

    void deleteAvatarById(Long id);


    User findByToken(String token);

    Resource findAvatarByToken(String token);

    User updateByToken(String token);

    Resource updateAvatarByToken(String token, MultipartFile avatar);

    void deleteByToken(String token);

    void deleteAvatarByToken(String token);

}

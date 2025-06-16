package dev.luiiscarlos.academ_iq_api.features.auth.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.features.user.model.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserId(long userId);

    void deleteByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    Boolean existsByToken(String token);

    Boolean existsByTokenAndUserId(String token, Long id);

}

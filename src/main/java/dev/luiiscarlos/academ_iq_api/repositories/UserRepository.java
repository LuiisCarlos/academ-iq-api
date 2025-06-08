package dev.luiiscarlos.academ_iq_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @NonNull
    @EntityGraph(attributePaths = { "enrollments" })
    Optional<User> findById(@NonNull Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("SELECT rt.user FROM RefreshToken rt WHERE rt.token = :token")
    Optional<User> findByToken(@Param("token") String token);

    Boolean existsByUsername(String username);

}

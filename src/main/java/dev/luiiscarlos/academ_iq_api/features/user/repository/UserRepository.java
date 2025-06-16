package dev.luiiscarlos.academ_iq_api.features.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.features.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);

    @EntityGraph(attributePaths = { "enrollments", "avatar"})
    Optional<User> findById(long id);

    @EntityGraph(attributePaths = { "enrollments", "authorities", "avatar" })
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    void deleteById(long id);

}

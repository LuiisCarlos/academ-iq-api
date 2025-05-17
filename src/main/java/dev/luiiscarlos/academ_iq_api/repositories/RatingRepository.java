package dev.luiiscarlos.academ_iq_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.models.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    Optional<Rating> findByUserIdAndCourseId(Long userId, Long courseId);

}

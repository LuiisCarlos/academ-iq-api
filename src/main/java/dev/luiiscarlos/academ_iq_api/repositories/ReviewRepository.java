package dev.luiiscarlos.academ_iq_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.models.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    Optional<Review> findByUserIdAndCourseId(Long userId, Long courseId);

}

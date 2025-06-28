package dev.luiiscarlos.academ_iq_api.features.learning.review.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.features.learning.review.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByCourseId(Pageable pageable, Long courseId);

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    Optional<Review> findByUserIdAndCourseId(Long userId, Long courseId);

}

package dev.luiiscarlos.academ_iq_api.features.learning.review.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.features.learning.review.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = { "user", "course" })
    Page<Review> findAllByCourseId(Pageable pageable, long courseId);

    @EntityGraph(attributePaths = { "user", "course" })
    Optional<Review> findByUserIdAndCourseId(long userId, long courseId);

    boolean existsByUserIdAndCourseId(long userId, long courseId);

}

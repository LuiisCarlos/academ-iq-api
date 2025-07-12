package dev.luiiscarlos.academ_iq_api.features.learning.category.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.features.learning.category.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @NonNull
    @EntityGraph(attributePaths = { "benefits" })
    Page<Category> findAll(@NonNull Pageable pageable);

    @EntityGraph(attributePaths = { "benefits" })
    Optional<Category> findByName(String name);

}

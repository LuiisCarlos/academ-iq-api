package dev.luiiscarlos.academ_iq_api.features.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @NonNull
    @EntityGraph(attributePaths = { "benefits" })
    List<Category> findAll();

    Optional<Category> findByName(String name);

}

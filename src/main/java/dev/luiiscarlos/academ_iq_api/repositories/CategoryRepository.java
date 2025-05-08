package dev.luiiscarlos.academ_iq_api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @SuppressWarnings("null") // TODO: Review this
    List<Category> findAll();

    Optional<Category> findByName(String name);

}

package dev.luiiscarlos.academ_iq_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.models.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByFilename(String filename);

    Optional<File> findByUrl(String url);

    int deleteByFilename(String filename);

    Boolean existsByFilename(String filename);

}

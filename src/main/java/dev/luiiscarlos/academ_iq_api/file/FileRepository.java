package dev.luiiscarlos.academ_iq_api.file;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByFilename(String filename);

    Optional<File> findByUrl(String url);

    int deleteByFilename(String filename);

    Boolean existsByFilename(String filename);

}

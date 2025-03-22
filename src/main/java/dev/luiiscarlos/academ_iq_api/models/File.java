package dev.luiiscarlos.academ_iq_api.models;

import java.time.LocalDateTime;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class File {

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String filename;

    @Nullable
    @Column(name = "content_type")
    private String contentType;

    @NonNull
    private Long size;

    @NonNull
    private String url;

    @Nullable
    private String extension;

    @NonNull
    @Builder.Default
    @Column(name = "is_image")
    private Boolean isImage = true;

    @Nullable
    @Builder.Default
    @Column(name = "is_default_file")
    private Boolean isDefaultFile = false;

    @Nullable
    @Builder.Default
    @Column(name = "updated_at")
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Boolean isImage() {
        return this.isImage;
    }

    public void setImage(Boolean isImage) {
        this.isImage = isImage;
    }

    public Boolean isDefaultFile() {
        return this.isDefaultFile;
    }

    public void setDefaultFile(Boolean isDefaultFile) {
        this.isDefaultFile = isDefaultFile;
    }

}

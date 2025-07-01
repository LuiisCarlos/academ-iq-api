package dev.luiiscarlos.academ_iq_api.features.storage.model;

import java.time.Duration;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    @Column(name = "content_type")
    private String contentType;

    private String url;

    private String extension;

    private Long size;

    @Builder.Default
    @Column(name = "is_image")
    private Boolean image = true;

    private Duration duration;

    @Builder.Default
    @Column(name = "is_default")
    private Boolean primary = false;

    @Column(name = "updated_at")
    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(name = "saved_at")
    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime saved_at = LocalDateTime.now();

    public Boolean isImage() {
        return this.image;
    }

    public Boolean isPrimary() {
        return this.primary;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}

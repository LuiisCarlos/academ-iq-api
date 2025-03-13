package dev.luiiscarlos.academ_iq_api.models;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
public class Course {

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private String author;

    @Nullable
    @Builder.Default
    @Column(name = "thumbnail_url")
    private String thumbnailUrl = "http://localhost:8888/api/v1/files/default-course-thumbnail.jpg";

    @Nullable
    @Builder.Default
    @Column(name = "video_url")
    private String videoUrl = null;

    @Nullable
    private List<String> requirements;

    @NonNull
    private String category;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Level level;

    @NonNull
    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime duration;

    @Nullable
    @Builder.Default
    @Column(name = "created_at")
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Nullable
    @Builder.Default
    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments = List.of();

}

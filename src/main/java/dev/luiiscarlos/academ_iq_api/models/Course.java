package dev.luiiscarlos.academ_iq_api.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.lang.NonNull;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @NonNull
    @Column(name = "video_url")
    private String videoUrl;

    @NonNull
    private String category;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Level level;

    @NonNull
    private LocalTime duration;

    @NonNull
    @Column(name = "uploaded_at")
    private LocalDate uploadedAt;

    @NonNull
    @OneToMany(mappedBy = "course")
    private List<UserCourse> users;

}

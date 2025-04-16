package dev.luiiscarlos.academ_iq_api.models;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;

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
    @JoinColumn(name = "file_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private File thumbnail;

    @Nullable
    @Builder.Default
    @ElementCollection
    @Column(name = "requirement")
    @CollectionTable(
        name = "course_requirement_junction",
        joinColumns = @JoinColumn(name = "course_id"))
    private List<String> requirements = new ArrayList<>();

    @NonNull
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Category category;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Level level;

    @Nullable
    @Builder.Default
    private Double rating = 0.0;

    @Nullable
    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    @Nullable
    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    @Nullable
    @Builder.Default
    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime duration = LocalTime.of(0, 0, 0);

    @Nullable
    @Builder.Default
    @Column(name = "created_at")
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

}

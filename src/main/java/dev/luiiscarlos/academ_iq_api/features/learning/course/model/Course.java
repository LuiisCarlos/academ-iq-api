package dev.luiiscarlos.academ_iq_api.features.learning.course.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.features.learning.category.model.Category;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.Section;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.Enrollment;
import dev.luiiscarlos.academ_iq_api.features.learning.review.model.Review;
import dev.luiiscarlos.academ_iq_api.features.storage.model.File;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;

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
import jakarta.persistence.Transient;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;

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
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "thumbnail_id")
    private File thumbnail;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;

    private String subtitle;

    @Column(length = 700)
    private String description;

    @Enumerated(EnumType.STRING)
    private CourseAccess access;

    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    private Double price;

    @Transient
    private Double rating;

    @Builder.Default
    @JsonFormat(shape = Shape.STRING)
    private Duration duration = Duration.ZERO;

    @Builder.Default
    @Column(name = "requirement")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "requirements", joinColumns = @JoinColumn(name = "course_id"))
    private Set<String> requirements = new LinkedHashSet<>();

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Section> sections = new LinkedHashSet<>();

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Enrollment> enrollments = new HashSet<>();

    @Column(name = "updated_at")
    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(name = "created_at")
    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Double getRating() {
        return calculateAverageRating();
    }

    private Double calculateAverageRating() {
        if (this.reviews == null || this.reviews.isEmpty())
            return 0.0;

        double sum = 0.0;
        for (Review rating : this.reviews)
            sum += rating.getRating();

        return sum / this.reviews.size();
    }

}

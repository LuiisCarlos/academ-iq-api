package dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "user_course_enrollments")
public class Enrollment {

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder.Default
    private Double progress = 0.0;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private ProgressState state = new ProgressState();

    @Builder.Default
    @Column(name = "is_favorite")
    private Boolean favorite = false;

    @Builder.Default
    @Column(name = "is_archived")
    private Boolean archived = false;

    @Builder.Default
    @Column(name = "is_completed")
    private Boolean completed = false;

    @Column(name = "completed_at")
    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime completedAt;

    @Column(name = "updated_at")
    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(name = "enrolled_at")
    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime enrolledAt = LocalDateTime.now();

    public Boolean isFavorite() {
        return this.favorite;
    }

    public Boolean isArchived() {
        return this.archived;
    }

    public Boolean isCompleted() {
        return this.completed;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}

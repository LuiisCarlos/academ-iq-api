package dev.luiiscarlos.academ_iq_api.features.enrollment.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.features.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "user_course_enrollments")
public class Enrollment {

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder.Default
    private Double progress = 0.0;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private ProgressState progressState = new ProgressState();

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

}

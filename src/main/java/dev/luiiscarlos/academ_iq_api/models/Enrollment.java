package dev.luiiscarlos.academ_iq_api.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.models.dtos.enrollment.ProgressState;
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

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder.Default
    private Double progress = 0.0;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private ProgressState progressState = new ProgressState();

    @Nullable
    @Builder.Default
    @Column(name = "is_favorite")
    private Boolean isFavorite = false;

    @Nullable
    @Builder.Default
    @Column(name = "is_archived")
    private Boolean isArchived = false;

    @Nullable
    @Builder.Default
    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Nullable
    @Builder.Default
    @Column(name = "enrolled_at")
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime enrolledAt = LocalDateTime.now();

    @Nullable
    @Column(name = "completed_at")
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime completedAt;

    public Boolean isFavorite() {
        return this.isFavorite;
    }

    public Boolean isArchived() {
        return this.isArchived;
    }

    public Boolean isCompleted() {
        return this.isCompleted;
    }

}

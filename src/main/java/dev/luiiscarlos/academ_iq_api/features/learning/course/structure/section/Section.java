package dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section;

import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.Lesson;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "course_id")
    private Course course;

    private String name;

    @Builder.Default
    @JsonFormat(shape = Shape.STRING)
    private Duration duration = Duration.ZERO;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY , cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lesson> lessons = new LinkedHashSet<>();

}

package dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressState {

    @Builder.Default
    private Long currentSectionId = null;

    @Builder.Default
    private Long currentLessonId = null;

    @Builder.Default
    private Set<CompletedLesson> completedLessons = new HashSet<>();

    public void addCompletedLesson(Long sectionId, Long lessonId) {
        this.completedLessons.add(CompletedLesson.builder()
                .sectionId(sectionId)
                .lessonId(lessonId)
                .completedAt(LocalDateTime.now())
                .build());
    }

    public boolean isLessonCompleted(Long sectionId, Long lessonId) {
        return this.completedLessons.stream()
                .anyMatch(cl -> cl.getSectionId().equals(sectionId) &&
                        cl.getLessonId().equals(lessonId));
    }

}
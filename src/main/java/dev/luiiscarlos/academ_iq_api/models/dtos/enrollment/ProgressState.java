package dev.luiiscarlos.academ_iq_api.models.dtos.enrollment;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressState {
    // Última lección vista
    @Builder.Default
    private Long currentSectionId = null;

    @Builder.Default
    private Long currentLessonId = null;

    // Lecciones completadas (Set para evitar duplicados)
    @Builder.Default
    private Set<CompletedLesson> completedLessons = new HashSet<>();

    // Método para agregar una lección completada
    public void addCompletedLesson(Long sectionId, Long lessonId) {
        this.completedLessons.add(
            CompletedLesson.builder()
                .sectionId(sectionId)
                .lessonId(lessonId)
                .completedAt(LocalDateTime.now())
                .build()
        );
    }

    // Verificar si una lección está completada
    public boolean isLessonCompleted(Long sectionId, Long lessonId) {
        return this.completedLessons.stream()
            .anyMatch(cl -> cl.getSectionId().equals(sectionId) &&
                           cl.getLessonId().equals(lessonId));
    }
}
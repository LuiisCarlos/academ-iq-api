package dev.luiiscarlos.academ_iq_api.features.enrollment.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletedLesson {

    private Long sectionId;

    private Long lessonId;

    @Builder.Default
    private LocalDateTime completedAt = LocalDateTime.now();
}

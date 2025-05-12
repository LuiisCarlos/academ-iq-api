package dev.luiiscarlos.academ_iq_api.models.dtos.lesson;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

@Data
public class LessonProgressDto {

    private Long lessonId;

    private boolean completed;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime lastAccessed;

    private double videoProgress;

}
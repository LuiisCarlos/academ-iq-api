package dev.luiiscarlos.academ_iq_api.models.dtos;

import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponseDto {

    private String name;

    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime duration;

    private List<LessonResponseDto> lessons;

}

package dev.luiiscarlos.academ_iq_api.models.dtos;

import java.util.List;

import lombok.Data;

@Data
public class SectionRequestDto {

    private String name;

    private String duration;

    private List<LessonRequestDto> lessons;

}

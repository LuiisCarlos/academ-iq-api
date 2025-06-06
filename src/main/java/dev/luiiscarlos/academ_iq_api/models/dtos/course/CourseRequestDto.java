package dev.luiiscarlos.academ_iq_api.models.dtos.course;

import java.util.List;

import dev.luiiscarlos.academ_iq_api.models.dtos.section.SectionRequestDto;
import lombok.Data;

@Data
public class CourseRequestDto {

    private Long userId;

    private String title;

    private String subtitle;

    private String description;

    private List<String> requirements;

    private Long categoryId;

    private String level;

    private List<SectionRequestDto> sections;

}

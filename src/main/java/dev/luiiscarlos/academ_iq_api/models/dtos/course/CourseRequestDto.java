package dev.luiiscarlos.academ_iq_api.models.dtos.course;

import java.util.List;

import dev.luiiscarlos.academ_iq_api.models.dtos.section.SectionRequestDto;
import lombok.Data;

@Data
public class CourseRequestDto {

    private Long userId;

    private Long categoryId;

    private String title;

    private String subtitle;

    private String description;

    private String level;

    private List<String> requirements;

    private List<SectionRequestDto> sections;

}

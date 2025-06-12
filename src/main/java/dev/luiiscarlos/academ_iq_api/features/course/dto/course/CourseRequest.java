package dev.luiiscarlos.academ_iq_api.features.course.dto.course;

import java.util.List;

import dev.luiiscarlos.academ_iq_api.features.course.dto.section.SectionRequest;

import lombok.Data;

@Data
public class CourseRequest {

    private Long userId;

    private Long categoryId;

    private String title;

    private String subtitle;

    private String description;

    private String level;

    private List<String> requirements;

    private List<SectionRequest> sections;

}

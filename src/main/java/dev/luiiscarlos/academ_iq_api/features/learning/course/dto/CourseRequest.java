package dev.luiiscarlos.academ_iq_api.features.learning.course.dto;

import java.util.List;

import dev.luiiscarlos.academ_iq_api.features.learning.course.model.CourseAccess;
import dev.luiiscarlos.academ_iq_api.features.learning.course.model.CourseLevel;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.section.SectionRequest;

import lombok.Data;

@Data
public class CourseRequest {

    private Long userId;

    private Long categoryId;

    private String title;

    private String subtitle;

    private String description;

    private CourseAccess access;

    private CourseLevel level;

    private List<String> requirements;

    private List<SectionRequest> sections;

}

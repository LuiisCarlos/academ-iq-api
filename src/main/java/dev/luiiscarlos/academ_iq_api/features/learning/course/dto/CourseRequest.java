package dev.luiiscarlos.academ_iq_api.features.learning.course.dto;

import java.util.Set;

import dev.luiiscarlos.academ_iq_api.features.learning.course.model.CourseAccess;
import dev.luiiscarlos.academ_iq_api.features.learning.course.model.CourseLevel;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.dto.SectionRequest;

import lombok.Data;

@Data
public class CourseRequest {

    private Long instructorId;

    private Long categoryId;

    private String title;

    private String subtitle;

    private String description;

    private CourseAccess access;

    private CourseLevel level;

    private Set<String> requirements;

    private Set<SectionRequest> sections;

}

package dev.luiiscarlos.academ_iq_api.models.mappers;

import org.springframework.stereotype.Component;
import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.models.Level;
import dev.luiiscarlos.academ_iq_api.models.dtos.CourseRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.CourseResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.EnrollmentCourseResponseDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final SectionMapper sectionMapper;

    public Course toCourse(CourseRequestDto courseRequest, File thumbnail) {
        return Course.builder()
            .name(courseRequest.getName())
            .description(courseRequest.getDescription())
            .author(courseRequest.getAuthor())
            .thumbnail(thumbnail)
            .requirements(courseRequest.getRequirements())
            .category(courseRequest.getCategory())
            .level(Level.valueOf(courseRequest.getLevel()))
            .build();
    }

    @SuppressWarnings("null") // Already handled
    public CourseResponseDto toCourseResponseDto(Course course) {
        return CourseResponseDto.builder()
            .name(course.getName())
            .description(course.getDescription())
            .author(course.getAuthor())
            .thumbnailUrl(course.getThumbnail().getUrl())
            .requirements(course.getRequirements())
            .category(course.getCategory())
            .sections(course.getSections().stream().map(sectionMapper::toSectionResponseDto).toList())
            .rating(course.getRating())
            .level(course.getLevel().toString())
            .duration(course.getDuration())
            .createdAt(course.getCreatedAt())
            .build();
    }

    @SuppressWarnings("null") // Already handled
    public EnrollmentCourseResponseDto toEnrollmentCourseResponseDto(Course course) {
        return EnrollmentCourseResponseDto.builder()
            .name(course.getName())
            .author(course.getAuthor())
            .thumbnailUrl(course.getThumbnail().getUrl())
            .category(course.getCategory())
            .build();
    }

}

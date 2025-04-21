package dev.luiiscarlos.academ_iq_api.models.mappers;

import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.Lesson;
import dev.luiiscarlos.academ_iq_api.models.Section;
import dev.luiiscarlos.academ_iq_api.models.dtos.CourseResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.EnrollmentCourseResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.LessonResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.SectionResponseDto;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    @SuppressWarnings("null")
    public CourseResponseDto toCourseResponseDto(Course course) {
        return CourseResponseDto.builder()
            .name(course.getName())
            .description(course.getDescription())
            .author(course.getAuthor())
            .thumbnailUrl(course.getThumbnail().getUrl())
            .requirements(course.getRequirements())
            .category(course.getCategory())
            .sections(course.getSections().stream().map(this::toSectionResponseDto).toList())
            .rating(course.getRating())
            .level(course.getLevel().toString())
            .duration(course.getDuration())
            .createdAt(course.getCreatedAt())
            .build();
    }

    @SuppressWarnings("null")
    private SectionResponseDto toSectionResponseDto(Section section) {
        return SectionResponseDto.builder()
            .name(section.getName())
            .lessons(section.getLessons().stream().map(this::toLessonResponseDto).toList())
            .build();
    }

    @SuppressWarnings("null")
    private LessonResponseDto toLessonResponseDto(Lesson lesson) {
        return LessonResponseDto.builder()
            .name(lesson.getName())
            .lessonUrl(lesson.getFile().getUrl())
            .build();
    }

    @SuppressWarnings("null")
    public EnrollmentCourseResponseDto toEnrollmentCourseResponseDto(Course course) {
        return EnrollmentCourseResponseDto.builder()
            .name(course.getName())
            .author(course.getAuthor())
            .thumbnailUrl(course.getThumbnail().getUrl())
            .category(course.getCategory())
            .build();
    }

}

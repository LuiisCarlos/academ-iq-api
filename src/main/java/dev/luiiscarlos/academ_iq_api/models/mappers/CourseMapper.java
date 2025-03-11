package dev.luiiscarlos.academ_iq_api.models.mappers;

import java.time.LocalTime;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.Level;
import dev.luiiscarlos.academ_iq_api.models.dtos.CourseRequestResponseDto;

@Component
public class CourseMapper {

    public Course toCourse(CourseRequestResponseDto course) {
        return Course.builder()
            .name(course.getName())
            .description(course.getDescription())
            .author(course.getAuthor())
            .category(course.getCategory())
            .level(Level.valueOf(course.getLevel()))
            .duration(LocalTime.parse(course.getDuration()))
            .build();
    }

    public Course toCourse(CourseRequestResponseDto course, String videoUrl) {
        return Course.builder()
            .name(course.getName())
            .description(course.getDescription())
            .author(course.getAuthor())
            .videoUrl(videoUrl)
            .category(course.getCategory())
            .level(Level.valueOf(course.getLevel()))
            .duration(LocalTime.parse(course.getDuration()))
            .build();
    }

    public Course toCourse(CourseRequestResponseDto course, String thumbnailUrl, String videoUrl) {
        return Course.builder()
            .name(course.getName())
            .description(course.getDescription())
            .author(course.getAuthor())
            .thumbnailUrl(thumbnailUrl)
            .videoUrl(videoUrl)
            .category(course.getCategory())
            .level(Level.valueOf(course.getLevel()))
            .duration(LocalTime.parse(course.getDuration()))
            .build();
    }

    public CourseRequestResponseDto toCourseRequestResponseDto(Course course) {
        return CourseRequestResponseDto.builder()
            .name(course.getName())
            .description(course.getDescription())
            .author(course.getAuthor())
            .thumbnailUrl(course.getThumbnailUrl())
            .videoUrl(course.getVideoUrl())
            .category(course.getCategory())
            .level(course.getLevel().toString())
            .duration(course.getDuration().toString())
            .build();
    }

}

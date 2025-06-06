package dev.luiiscarlos.academ_iq_api.models.mappers;

import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.Lesson;
import dev.luiiscarlos.academ_iq_api.models.Review;
import dev.luiiscarlos.academ_iq_api.models.Section;
import dev.luiiscarlos.academ_iq_api.models.dtos.course.CourseResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.rating.ReviewResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.enrollment.EnrollmentCourseResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.lesson.LessonResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.section.SectionResponseDto;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final FileMapper fileMapper;

    public CourseResponseDto toCourseResponseDto(Course course) {
        String instructor = course.getInstructor().getFirstname() + " " + course.getInstructor().getLastname();
        String thumbnail = course.getThumbnail() != null ? course.getThumbnail().getUrl() : null;

        return CourseResponseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .subtitle(course.getSubtitle())
                .description(course.getDescription())
                .instructor(instructor)
                .thumbnail(thumbnail)
                .requirements(course.getRequirements())
                .category(course.getCategory())
                .sections(course.getSections().stream().map(this::toSectionResponseDto).toList())
                .averageRating(course.getAverageRating())
                .ratings(course.getRatings().stream().map(this::toRatingResponseDto).toList())
                .level(course.getLevel().toString())
                .duration(course.getDuration())
                .createdAt(course.getCreatedAt())
                .build();
    }

    @SuppressWarnings("null")
    private SectionResponseDto toSectionResponseDto(Section section) {
        return SectionResponseDto.builder()
                .id(section.getId())
                .name(section.getName())
                .duration(section.getDuration())
                .lessons(section.getLessons().stream().map(this::toLessonResponseDto).toList())
                .build();
    }

    private LessonResponseDto toLessonResponseDto(Lesson lesson) {
        return LessonResponseDto.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .file(fileMapper.toFileResponseDto(lesson.getFile()))
                .build();
    }

    private ReviewResponseDto toRatingResponseDto(Review rating) {
        return ReviewResponseDto.builder()
                .rating(rating.getRating())
                .comment(rating.getComment())
                .user(rating.getUser().getUsername())
                .userAvatarUrl(rating.getUser().getAvatar().getUrl())
                .ratedAt(rating.getRatedAt())
                .build();
    }

    public EnrollmentCourseResponseDto toEnrollmentCourseResponseDto(Course course) {
        String instructor = course.getInstructor().getFirstname() + " " + course.getInstructor().getLastname();

        return EnrollmentCourseResponseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .instructor(instructor)
                .thumbnailUrl(course.getThumbnail().getUrl())
                .category(course.getCategory().getName())
                .categorySvg(course.getCategory().getSvg())
                .duration(course.getDuration())
                .build();
    }

}

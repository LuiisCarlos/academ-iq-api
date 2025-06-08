package dev.luiiscarlos.academ_iq_api.models.mappers;

import dev.luiiscarlos.academ_iq_api.models.Category;
import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.Lesson;
import dev.luiiscarlos.academ_iq_api.models.Review;
import dev.luiiscarlos.academ_iq_api.models.Section;
import dev.luiiscarlos.academ_iq_api.models.dtos.course.CourseCategoryResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.course.CourseLessonResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.course.CourseResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.course.CourseSectionResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.course.PublicCourseResponseDto;
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

    public PublicCourseResponseDto toPublicResponseDto(Course course) {
        return PublicCourseResponseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .subtitle(course.getSubtitle())
                .description(course.getDescription())
                .instructor(course.getInstructor().getFullname())
                .thumbnail(course.getThumbnail().getUrl())
                .requirements(course.getRequirements())
                .category(this.toCourseCategoryResponseDto(course.getCategory()))
                .sections(course.getSections().size())
                .rating(course.getRating())
                .reviews(course.getReviews().size())
                .level(course.getLevel().toString())
                .duration(course.getDuration())
                .createdAt(course.getCreatedAt())
                .build();
    }

    public CourseResponseDto toResponseDto(Course course) {
        return CourseResponseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .subtitle(course.getSubtitle())
                .description(course.getDescription())
                .instructor(course.getInstructor().getFullname())
                .thumbnail(course.getThumbnail().getUrl())
                .requirements(course.getRequirements())
                .category(this.toCourseCategoryResponseDto(course.getCategory()))
                .sections(course.getSections().stream().map(this::toSectionResponseDto).toList())
                .rating(course.getRating())
                .reviews(course.getReviews().stream().map(this::toReviewResponseDto).toList())
                .level(course.getLevel().toString())
                .duration(course.getDuration())
                .createdAt(course.getCreatedAt())
                .build();
    }

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
                .video(fileMapper.toFileResponseDto(lesson.getVideo()))
                .build();
    }

    private CourseSectionResponseDto toCourseSectionResponseDto(Section section) {
        return CourseSectionResponseDto.builder()
                .id(section.getId())
                .name(section.getName())
                .duration(section.getDuration())
                .lessons(section.getLessons().stream().map(this::toCourseLessonResponseDto).toList())
                .build();
    }

    private CourseLessonResponseDto toCourseLessonResponseDto(Lesson lesson) {
        return CourseLessonResponseDto.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .build();
    }

    private ReviewResponseDto toReviewResponseDto(Review review) {
        return ReviewResponseDto.builder()
                .rating(review.getRating())
                .comment(review.getComment())
                .username(review.getUser().getUsername())
                .avatar(review.getUser().getAvatar().getUrl())
                .ratedAt(review.getRatedAt())
                .build();
    }

    public CourseCategoryResponseDto toCourseCategoryResponseDto(Category category) {
        return CourseCategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .shortDescription(category.getShortDescription())
                .build();
    }

    public EnrollmentCourseResponseDto toEnrollmentCourseResponseDto(Course course) {
        return EnrollmentCourseResponseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .instructor(course.getInstructor().getFullname())
                .thumbnailUrl(course.getThumbnail().getUrl())
                .category(course.getCategory().getName())
                .categorySvg(course.getCategory().getSvg())
                .duration(course.getDuration())
                .build();
    }

}

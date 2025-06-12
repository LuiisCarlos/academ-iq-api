package dev.luiiscarlos.academ_iq_api.features.course.mapper;

import dev.luiiscarlos.academ_iq_api.features.category.dto.CategoryResponse;
import dev.luiiscarlos.academ_iq_api.features.category.model.Category;
import dev.luiiscarlos.academ_iq_api.features.course.dto.course.CourseResponse;
import dev.luiiscarlos.academ_iq_api.features.course.dto.course.PublicCourseResponse;
import dev.luiiscarlos.academ_iq_api.features.course.dto.lesson.LessonResponse;
import dev.luiiscarlos.academ_iq_api.features.course.dto.section.SectionResponse;
import dev.luiiscarlos.academ_iq_api.features.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.course.model.Lesson;
import dev.luiiscarlos.academ_iq_api.features.course.model.Section;
import dev.luiiscarlos.academ_iq_api.features.enrollment.dto.EnrollmentCourseResponse;
import dev.luiiscarlos.academ_iq_api.features.file.mapper.FileMapper;
import dev.luiiscarlos.academ_iq_api.features.review.dto.ReviewResponse;
import dev.luiiscarlos.academ_iq_api.features.review.model.Review;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final FileMapper fileMapper;

    public PublicCourseResponse toPublicResponseDto(Course course) {
        return PublicCourseResponse.builder()
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
                .level(course.getLevel())
                .duration(course.getDuration())
                .updatedAt(course.getUpdatedAt())
                .createdAt(course.getCreatedAt())
                .build();
    }

    public CourseResponse toResponseDto(Course course) {
        return CourseResponse.builder()
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
                .level(course.getLevel())
                .duration(course.getDuration())
                .updatedAt(course.getUpdatedAt())
                .createdAt(course.getCreatedAt())
                .build();
    }

    private SectionResponse toSectionResponseDto(Section section) {
        return SectionResponse.builder()
                .id(section.getId())
                .name(section.getName())
                .duration(section.getDuration())
                .lessons(section.getLessons().stream().map(this::toLessonResponseDto).toList())
                .build();
    }

    private LessonResponse toLessonResponseDto(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .video(fileMapper.toFileResponseDto(lesson.getVideo()))
                .build();
    }

    /* private CourseSectionResponseDto toCourseSectionResponseDto(Section section) {
        return CourseSectionResponseDto.builder()
                .id(section.getId())
                .name(section.getName())
                .duration(section.getDuration())
                .lessons(section.getLessons().stream().map(this::toCourseLessonResponseDto).toList())
                .build();
    } */

    /* private CourseLessonResponseDto toCourseLessonResponseDto(Lesson lesson) {
        return CourseLessonResponseDto.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .build();
    } */

    private ReviewResponse toReviewResponseDto(Review review) {
        return ReviewResponse.builder()
                .rating(review.getRating())
                .comment(review.getComment())
                .username(review.getUser().getUsername())
                .avatar(review.getUser().getAvatar().getUrl())
                .ratedAt(review.getRatedAt())
                .build();
    }

    public CategoryResponse toCourseCategoryResponseDto(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .shortDescription(category.getShortDescription())
                .build();
    }

    public EnrollmentCourseResponse toEnrollmentCourseResponseDto(Course course) {
        return EnrollmentCourseResponse.builder()
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

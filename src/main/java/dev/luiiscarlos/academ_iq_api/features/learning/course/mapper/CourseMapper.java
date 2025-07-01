package dev.luiiscarlos.academ_iq_api.features.learning.course.mapper;

import dev.luiiscarlos.academ_iq_api.features.learning.category.mapper.CategoryMapper;
import dev.luiiscarlos.academ_iq_api.features.learning.category.model.Category;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CoursePublicResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.mapper.SectionMapper;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.dto.CourseEnrollmentResponse;
import dev.luiiscarlos.academ_iq_api.features.storage.model.File;
import dev.luiiscarlos.academ_iq_api.features.identity.user.mapper.UserMapper;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CourseMapper {

	private final SectionMapper sectionMapper;

	private final CategoryMapper categoryMapper;

	private final UserMapper userMapper;

	public Course toEntity(CourseRequest dto) {
		User instructor = new User();
		instructor.setId(dto.getInstructorId());

		Category category = new Category();
		category.setId(dto.getCategoryId());

		return Course.builder()
				.instructor(instructor)
				.category(category)
				.title(dto.getTitle())
				.subtitle(dto.getSubtitle())
				.description(dto.getDescription())
				.requirements(dto.getRequirements())
				.access(dto.getAccess())
				.level(dto.getLevel())
				.build();
	}

	public Course toEntity(CourseRequest dto, File file) {
		User instructor = new User();
		instructor.setId(dto.getInstructorId());

		Category category = new Category();
		category.setId(dto.getCategoryId());

		return Course.builder()
				.instructor(instructor)
				.category(category)
				.thumbnail(file)
				.title(dto.getTitle())
				.subtitle(dto.getSubtitle())
				.description(dto.getDescription())
				.requirements(dto.getRequirements())
				.access(dto.getAccess())
				.level(dto.getLevel())
				.build();
	}

	public CourseResponse toDto(Course entity) {
		return CourseResponse.builder()
				.id(entity.getId())
				.title(entity.getTitle())
				.subtitle(entity.getSubtitle())
				.description(entity.getDescription())
				.instructor(userMapper.toInstructorDto(entity.getInstructor()))
				.thumbnail(entity.getThumbnail().getUrl())
				.requirements(entity.getRequirements())
				.category(categoryMapper.toDto(entity.getCategory()))
				.price(entity.getPrice())
				.sections(entity.getSections().stream()
						.map(sectionMapper::toDto)
						.collect(Collectors.toSet()))
				.rating(entity.getRating())
				.reviews(entity.getReviews().size())
				.access(entity.getAccess())
				.level(entity.getLevel())
				.duration(entity.getDuration().toMillis())
				.updatedAt(entity.getUpdatedAt())
				.createdAt(entity.getCreatedAt())
				.build();
	}

	public CoursePublicResponse toPublicDto(Course entity) {
		return CoursePublicResponse.builder()
				.id(entity.getId())
				.title(entity.getTitle())
				.subtitle(entity.getSubtitle())
				.description(entity.getDescription())
				.instructor(entity.getInstructor().getFullname())
				.thumbnail(entity.getThumbnail().getUrl())
				.requirements(entity.getRequirements())
				.category(categoryMapper.toDto(entity.getCategory()))
				.sections(entity.getSections().size())
				.rating(entity.getRating())
				.price(entity.getPrice())
				.reviews(entity.getReviews().size())
				.access(entity.getAccess())
				.level(entity.getLevel())
				.duration(entity.getDuration().toMillis())
				.updatedAt(entity.getUpdatedAt())
				.createdAt(entity.getCreatedAt())
				.build();
	}

	public CourseEnrollmentResponse toEnrollmentDto(Course entity) {
		return CourseEnrollmentResponse.builder()
				.id(entity.getId())
				.title(entity.getTitle())
				.instructor(entity.getInstructor().getFullname())
				.thumbnailUrl(entity.getThumbnail().getUrl())
				.category(entity.getCategory().getName())
				.categorySvg(entity.getCategory().getSvg())
				.duration(entity.getDuration())
				.build();
	}

}

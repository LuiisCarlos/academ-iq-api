package dev.luiiscarlos.academ_iq_api.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.exceptions.CourseAlreadyExistsEception;
import dev.luiiscarlos.academ_iq_api.exceptions.CourseNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.models.Category;
import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.models.Lesson;
import dev.luiiscarlos.academ_iq_api.models.Level;
import dev.luiiscarlos.academ_iq_api.models.Section;
import dev.luiiscarlos.academ_iq_api.models.dtos.course.CourseRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.course.CourseResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.lesson.LessonRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.section.SectionRequestDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.CourseMapper;
import dev.luiiscarlos.academ_iq_api.repositories.CategoryRepository;
import dev.luiiscarlos.academ_iq_api.repositories.CourseRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final CategoryRepository categoryRepository;

    private final FileServiceImpl fileService;

    private final CourseMapper courseMapper;

    /**
     * Saves the course
     *
     * @param courseDto the course details
     * @param files     the files to be saved
     *
     * @return an {@link CourseResponseDto} as the saved course
     *
     * @throws CourseAlreadyExistsEception if the course already exists
     * @throws CourseNotFoundException     if the category does not exist
     * @throws InvalidFileTypeException    if the file is not a valid video
     */
    @SuppressWarnings("null")
    public CourseResponseDto save(CourseRequestDto courseDto, Map<String, MultipartFile> files) {
        if (courseRepository.existsByTitle(courseDto.getTitle()))
            throw new CourseAlreadyExistsEception(
                    "Failed to save course: Course already exists");

        File thumbnail;
        MultipartFile thumbPart = files.get("thumbnail");
        if (thumbPart != null && !thumbPart.isEmpty())
            thumbnail = fileService.save(thumbPart, false);
        else
            thumbnail = fileService.findByFilename("default-course-thumbnail.jpg");

        Course course = Course.builder()
                .title(courseDto.getTitle())
                .subtitle(courseDto.getSubtitle())
                .description(courseDto.getDescription())
                .author(courseDto.getAuthor())
                .requirements(courseDto.getRequirements())
                .category(categoryRepository.findById(courseDto.getCategoryId())
                        .orElseThrow(() -> new CourseNotFoundException(
                                "Failed to save course: Category not found")))
                .level(Level.valueOf(courseDto.getLevel()))
                .thumbnail(thumbnail)
                .sections(new ArrayList<>())
                .build();

        for (SectionRequestDto sectionDto : courseDto.getSections()) {
            Section section = Section.builder()
                    .name(sectionDto.getName())
                    .course(course)
                    .duration(LocalTime.parse(sectionDto.getDuration()))
                    .lessons(new ArrayList<>())
                    .build();

            for (LessonRequestDto lessonDto : sectionDto.getLessons()) {
                MultipartFile multipartFile = files.get("file_" + lessonDto.getName());

                if (multipartFile != null && !multipartFile.isEmpty()) {
                    if (!fileService.isValidVideo(multipartFile))
                        throw new InvalidFileTypeException(
                                "Invalid video content type: " + multipartFile.getContentType());
                }

                File video = (multipartFile != null && !multipartFile.isEmpty())
                        ? fileService.save(multipartFile, false)
                        : null;

                Lesson lesson = Lesson.builder()
                        .name(lessonDto.getName())
                        .file(video)
                        .section(section)
                        .build();

                section.getLessons().add(lesson);
            }
            course.getSections().add(section);
        }

        course = courseRepository.save(course);
        return courseMapper.toCourseResponseDto(course);
    }

    /**
     * Find all courses
     *
     * @return the list of the courses
     *
     * @throws CourseNotFoundException if no courses are found
     */
    public List<CourseResponseDto> findAll() {
        List<Course> courses = courseRepository.findAll();

        if (courses.isEmpty())
            throw new CourseNotFoundException(
                    "Failed to find courses: No courses found");

        return courses.stream().map(courseMapper::toCourseResponseDto).toList();
    }

    /**
     * Find all categories
     *
     * @return the list of the courses
     *
     * @throws CourseNotFoundException if no categories are found
     */
    public List<Category> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty())
            throw new CourseNotFoundException(
                    "Failed to find category: No categories found");

        return categories;
    }

    public Category findCategoryByName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CourseNotFoundException(
                        "Failed to find category: Category not found with name " + categoryName));

        return category;
    }

    /**
     * Finds the course by its id
     *
     * @param id the id of the course
     *
     * @return the course
     *
     * @throws CourseNotFoundException if the course is not found
     */
    public Course findById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(
                        "Failed to find course: Course not found with id " + courseId));
    }

    /**
     * Updates the course by its id
     *
     * @param courseId  the id of the course
     * @param courseDto the course details
     * @param files     the files to be saved
     *
     * @return the course
     *
     * @throws CourseNotFoundException  if the course is not found
     * @throws InvalidFileTypeException if the file is not a valid video
     */
    @SuppressWarnings("null")
    public CourseResponseDto updateById(
            Long courseId,
            CourseRequestDto courseDto,
            Map<String, MultipartFile> files) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));

        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setAuthor(courseDto.getAuthor());
        course.setRequirements(courseDto.getRequirements());
        course.setLevel(Level.valueOf(courseDto.getLevel()));
        course.setCategory(categoryRepository.findById(courseDto.getCategoryId())
                .orElseThrow(() -> new CourseNotFoundException(
                        "Failed to save course: Category not found with id " + courseDto.getCategoryId())));

        MultipartFile thumbPart = files.get("thumbnail");

        List<String> filenamesToDelete = new ArrayList<>();

        if (thumbPart != null && !thumbPart.isEmpty()) {
            filenamesToDelete.add(course.getThumbnail().getFilename());

            File thumbnail = fileService.save(thumbPart, false);
            thumbnail.setUpdatedAt(LocalDateTime.now());
            course.setThumbnail(thumbnail);
        }

        for (Section section : course.getSections()) {
            for (Lesson lesson : section.getLessons()) {
                File oldFile = lesson.getFile();
                if (oldFile != null && !oldFile.isDefaultFile()) {
                    lesson.setFile(null);
                    filenamesToDelete.add(oldFile.getFilename());
                }
            }
        }

        course.getSections().clear();
        for (SectionRequestDto sectionDto : courseDto.getSections()) {
            Section section = Section.builder()
                    .name(sectionDto.getName())
                    .course(course)
                    .lessons(new ArrayList<>())
                    .build();

            for (LessonRequestDto lessonDto : sectionDto.getLessons()) {
                MultipartFile multipartFile = files.get("file_" + lessonDto.getName());

                if (multipartFile != null && !multipartFile.isEmpty()) {
                    if (!fileService.isValidVideo(multipartFile))
                        throw new InvalidFileTypeException(
                                "Invalid video content type: " + multipartFile.getContentType());
                }

                File video = (multipartFile != null && !multipartFile.isEmpty())
                        ? fileService.save(multipartFile, false)
                        : null;

                Lesson lesson = Lesson.builder()
                        .name(lessonDto.getName())
                        .file(video)
                        .section(section)
                        .build();

                section.getLessons().add(lesson);
            }

            course.getSections().add(section);
        }

        Course updated = courseRepository.save(course);

        for (String filename : filenamesToDelete)
            fileService.deleteByFilename(filename);

        return courseMapper.toCourseResponseDto(updated);
    }

    /**
     * Deletes the course by its id
     *
     * @param id the id of the course
     *
     * @throws CourseNotFoundException if the course is not found
     */
    public void deleteById(Long courseId) {
        if (!courseRepository.existsById(courseId))
            throw new CourseNotFoundException(
                    "Failed to find a course: Course not found with id " + courseId);

        courseRepository.deleteById(courseId);
    }

}

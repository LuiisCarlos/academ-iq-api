package dev.luiiscarlos.academ_iq_api.features.course.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.core.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.features.category.model.Category;
import dev.luiiscarlos.academ_iq_api.features.course.dto.CourseRequest;
import dev.luiiscarlos.academ_iq_api.features.course.dto.CourseResponse;
import dev.luiiscarlos.academ_iq_api.features.course.dto.PublicCourseResponse;
import dev.luiiscarlos.academ_iq_api.features.course.exception.CourseAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.features.course.exception.CourseNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.course.mapper.CourseMapper;
import dev.luiiscarlos.academ_iq_api.features.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.course.model.Course.Level;
import dev.luiiscarlos.academ_iq_api.features.course.repository.CourseRepository;
import dev.luiiscarlos.academ_iq_api.features.course.structure.lesson.Lesson;
import dev.luiiscarlos.academ_iq_api.features.course.structure.lesson.dto.LessonRequest;
import dev.luiiscarlos.academ_iq_api.features.course.structure.section.Section;
import dev.luiiscarlos.academ_iq_api.features.course.structure.section.section.SectionRequest;
import dev.luiiscarlos.academ_iq_api.features.file.exception.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.features.file.model.File;
import dev.luiiscarlos.academ_iq_api.features.file.service.FileService;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;
import dev.luiiscarlos.academ_iq_api.shared.enums.FileType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseService { // TODO Fix documentation

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    private final FileService fileService;

    /**
     * Saves the course details and the files associated with it
     *
     * @param request the course details
     * @param files   the files to be saved
     * @return {@link CourseResponse} the saved course
     * @throws CourseAlreadyExistsException if the course already exists
     * @throws InvalidFileTypeException     if the file is not a valid video
     */
    public CourseResponse create(CourseRequest request, Map<String, MultipartFile> files) {
        if (courseRepository.existsByTitle(request.getTitle()))
            throw new CourseAlreadyExistsException(
                    String.format(ErrorMessages.COURSE_ALREADY_EXISTS, request.getTitle()));

        File thumbnail;
        MultipartFile thumbPart = files.get("thumbnail");
        if (thumbPart != null && !thumbPart.isEmpty())
            thumbnail = fileService.create(thumbPart, FileType.THUMBNAIL);
        else
            thumbnail = fileService.get("default-course-thumbnail.jpg");

        Course course = Course.builder()
                .instructor(User.builder().id(request.getUserId()).build())
                .category(Category.builder().id(request.getCategoryId()).build())
                .title(request.getTitle())
                .subtitle(request.getSubtitle())
                .description(request.getDescription())
                .requirements(request.getRequirements())
                .level(Level.valueOf(request.getLevel()))
                .thumbnail(thumbnail)
                .sections(new ArrayList<>())
                .build();

        for (SectionRequest sectionDto : request.getSections()) {
            Section section = Section.builder()
                    .name(sectionDto.getName())
                    .course(course)
                    .duration(Duration.ofSeconds(sectionDto.getDuration()))
                    .lessons(new ArrayList<>())
                    .build();

            for (LessonRequest lessonDto : sectionDto.getLessons()) {
                MultipartFile multipartFile = files.get("file_" + lessonDto.getName());

                if (multipartFile != null && !multipartFile.isEmpty()) {
                    if (!fileService.isValidVideo(multipartFile))
                        throw new InvalidFileTypeException(
                                "Invalid video content type: " + multipartFile.getContentType());
                }

                File video = (multipartFile != null && !multipartFile.isEmpty())
                        ? fileService.create(multipartFile, FileType.THUMBNAIL)
                        : null;

                Lesson lesson = Lesson.builder()
                        .name(lessonDto.getName())
                        .video(video)
                        .section(section)
                        .build();

                section.getLessons().add(lesson);
            }
            course.getSections().add(section);
        }

        course = courseRepository.save(course);
        return courseMapper.toResponseDto(course);
    }

    /**
     * Find all courses
     *
     * @return the list of the courses
     * @throws CourseNotFoundException if no courses are found
     */
    public List<PublicCourseResponse> findAll() {
        List<Course> courses = courseRepository.findAll();

        if (courses.isEmpty())
            throw new CourseNotFoundException(
                    "No courses found");

        return courses.stream().map(courseMapper::toPublicResponseDto).toList();
    }

    /**
     *
     * @param courseId
     * @return
     */
    public List<Long> findAllLessonIdsById(long courseId) {
        List<Long> lessonsIds = courseRepository.findAllLessonIdsById(courseId);

        if (lessonsIds.isEmpty())
            throw new CourseNotFoundException("No lessons found with course id: " + courseId);

        return lessonsIds;
    }

    /**
     * Finds the course by its id
     *
     * @param id the id of the course
     * @return the course
     * @throws CourseNotFoundException if the course is not found
     */
    public Course findById(long courseId) {
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
     * @return the course
     * @throws CourseNotFoundException  if the course is not found
     * @throws InvalidFileTypeException if the file is not a valid video
     */
    public CourseResponse updateById(long courseId, CourseRequest request, Map<String, MultipartFile> files) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));

        course.setInstructor(User.builder().id(request.getUserId()).build());
        course.setCategory(Category.builder().id(request.getCategoryId()).build());
        course.setTitle(request.getTitle());
        course.setSubtitle(request.getSubtitle());
        course.setDescription(request.getDescription());
        course.setRequirements(request.getRequirements());
        course.setLevel(Level.valueOf(request.getLevel()));

        MultipartFile thumbPart = files.get("thumbnail");

        List<String> filenamesToDelete = new ArrayList<>();

        if (thumbPart != null && !thumbPart.isEmpty()) {
            filenamesToDelete.add(course.getThumbnail().getFilename());

            File thumbnail = fileService.create(thumbPart, FileType.THUMBNAIL);
            thumbnail.setUpdatedAt(LocalDateTime.now());
            course.setThumbnail(thumbnail);
        }

        for (Section section : course.getSections()) {
            for (Lesson lesson : section.getLessons()) {
                File oldFile = lesson.getVideo();
                if (oldFile != null && !oldFile.isPrimary()) {
                    lesson.setVideo(null);
                    filenamesToDelete.add(oldFile.getFilename());
                }
            }
        }

        course.getSections().clear();
        for (SectionRequest sectionDto : request.getSections()) {
            Section section = Section.builder()
                    .name(sectionDto.getName())
                    .course(course)
                    .lessons(new ArrayList<>())
                    .build();

            for (LessonRequest lessonDto : sectionDto.getLessons()) {
                MultipartFile multipartFile = files.get("file_" + lessonDto.getName());

                if (multipartFile != null && !multipartFile.isEmpty()) {
                    if (!fileService.isValidVideo(multipartFile))
                        throw new InvalidFileTypeException(String.format(
                                ErrorMessages.INVALID_CONTENT_TYPE, multipartFile.getContentType()));
                }

                File video = (multipartFile != null && !multipartFile.isEmpty())
                        ? fileService.create(multipartFile, FileType.COURSE_VIDEO)
                        : new File();

                Lesson lesson = Lesson.builder()
                        .name(lessonDto.getName())
                        .video(video)
                        .section(section)
                        .build();

                section.getLessons().add(lesson);
            }

            course.getSections().add(section);
        }

        Course updated = courseRepository.save(course);

        for (String filename : filenamesToDelete)
            fileService.get(filename);

        return courseMapper.toResponseDto(updated);
    }

    /**
     * Deletes the course by its id
     *
     * @param id the id of the course
     * @throws CourseNotFoundException if the course is not found
     */
    public void deleteById(Long courseId) {
        if (!courseRepository.existsById(courseId))
            throw new CourseNotFoundException(
                    "Failed to find a course: Course not found with id " + courseId);

        courseRepository.deleteById(courseId);
    }

}

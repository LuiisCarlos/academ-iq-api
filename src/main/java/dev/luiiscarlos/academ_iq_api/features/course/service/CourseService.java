package dev.luiiscarlos.academ_iq_api.features.course.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.core.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.features.category.Category;
import dev.luiiscarlos.academ_iq_api.features.course.dto.course.CourseRequestDto;
import dev.luiiscarlos.academ_iq_api.features.course.dto.course.CourseResponseDto;
import dev.luiiscarlos.academ_iq_api.features.course.dto.course.PublicCourseResponseDto;
import dev.luiiscarlos.academ_iq_api.features.course.dto.lesson.LessonRequestDto;
import dev.luiiscarlos.academ_iq_api.features.course.dto.section.SectionRequestDto;
import dev.luiiscarlos.academ_iq_api.features.course.exception.CourseAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.features.course.exception.CourseNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.course.mapper.CourseMapper;
import dev.luiiscarlos.academ_iq_api.features.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.course.model.Lesson;
import dev.luiiscarlos.academ_iq_api.features.course.model.Section;
import dev.luiiscarlos.academ_iq_api.features.course.model.Course.Level;
import dev.luiiscarlos.academ_iq_api.features.course.repository.CourseRepository;
import dev.luiiscarlos.academ_iq_api.features.file.exception.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.features.file.model.File;
import dev.luiiscarlos.academ_iq_api.features.file.service.FileService;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final FileService fileService;

    private final CourseMapper courseMapper;

    /**
     * Saves the course details and the files associated with it
     *
     * @param courseDto the course details
     * @param files     the files to be saved
     * @return {@link CourseResponseDto} the saved course
     * @throws CourseAlreadyExistsException if the course already exists
     * @throws CourseNotFoundException      if the category does not exist
     * @throws InvalidFileTypeException     if the file is not a valid video
     */
    public CourseResponseDto create(CourseRequestDto courseDto, Map<String, MultipartFile> files) {
        if (courseRepository.existsByTitle(courseDto.getTitle()))
            throw new CourseAlreadyExistsException(
                    String.format(ErrorMessages.COURSE_ALREADY_EXISTS, courseDto.getTitle()));

        File thumbnail;
        MultipartFile thumbPart = files.get("thumbnail");
        if (thumbPart != null && !thumbPart.isEmpty())
            thumbnail = fileService.save(thumbPart, "thumbnail", false);
        else
            thumbnail = fileService.findByFilename("default-course-thumbnail.jpg");

        Course course = Course.builder()
                .instructor(User.builder().id(courseDto.getUserId()).build())
                .category(Category.builder().id(courseDto.getCategoryId()).build())
                .title(courseDto.getTitle())
                .subtitle(courseDto.getSubtitle())
                .description(courseDto.getDescription())
                .requirements(courseDto.getRequirements())
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
                        ? fileService.save(multipartFile, "course", false)
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
    public List<PublicCourseResponseDto> findAll() {
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
    public List<Long> findAllLessonIdsById(Long courseId) {
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
     * @return the course
     * @throws CourseNotFoundException  if the course is not found
     * @throws InvalidFileTypeException if the file is not a valid video
     */
    public CourseResponseDto updateById(
            Long courseId,
            CourseRequestDto courseDto,
            Map<String, MultipartFile> files) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));

        course.setInstructor(User.builder().id(courseDto.getUserId()).build());
        course.setCategory(Category.builder().id(courseDto.getCategoryId()).build());
        course.setTitle(courseDto.getTitle());
        course.setSubtitle(courseDto.getSubtitle());
        course.setDescription(courseDto.getDescription());
        course.setRequirements(courseDto.getRequirements());
        course.setLevel(Level.valueOf(courseDto.getLevel()));

        MultipartFile thumbPart = files.get("thumbnail");

        List<String> filenamesToDelete = new ArrayList<>();

        if (thumbPart != null && !thumbPart.isEmpty()) {
            filenamesToDelete.add(course.getThumbnail().getFilename());

            File thumbnail = fileService.save(thumbPart, "thumbnail", false);
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
                        throw new InvalidFileTypeException(String.format(
                                ErrorMessages.INVALID_CONTENT_TYPE, multipartFile.getContentType()));
                }

                File video = (multipartFile != null && !multipartFile.isEmpty())
                        ? fileService.save(multipartFile, "course",  false)
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
            fileService.deleteByFilename(filename);

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

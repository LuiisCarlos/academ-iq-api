package dev.luiiscarlos.academ_iq_api.features.learning.course.service.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.shared.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.features.learning.category.model.Category;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CoursePublicResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.exception.CourseAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.features.learning.course.mapper.CourseMapper;
import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.learning.course.service.CourseService;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.Lesson;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.dto.LessonRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.Section;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.dto.SectionRequest;
import dev.luiiscarlos.academ_iq_api.features.storage.model.File;
import dev.luiiscarlos.academ_iq_api.features.storage.model.FileType;
import dev.luiiscarlos.academ_iq_api.features.storage.service.StorageService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseQueryService courseQueryService;

    private final CourseMapper courseMapper;

    private final StorageService storageService;

    public CourseResponse create(CourseRequest request, Map<String, MultipartFile> files) {
        if (courseQueryService.existsByTitle(request.getTitle()))
            throw new CourseAlreadyExistsException(
                    String.format(ErrorMessages.COURSE_ALREADY_EXISTS_BY_TITLE, request.getTitle()));

        File thumbnail;

        MultipartFile thumbPart = files.get("thumbnail");
        if (Objects.nonNull(thumbPart) && !thumbPart.isEmpty())
            thumbnail = storageService.create(thumbPart, FileType.THUMBNAIL);
        else
            thumbnail = storageService.get("default-course-thumbnail_jbaqrj");

        Course course = courseMapper.toEntity(request);
        course.setThumbnail(thumbnail);
        course.setSections(new LinkedHashSet<>());

        Duration courseDuration = Duration.ZERO;

        for (SectionRequest section : request.getSections()) {
            Section currentSection = new Section();
            currentSection.setName(section.getName());
            currentSection.setCourse(course);
            currentSection.setLessons(new LinkedHashSet<>());

            Duration sectionDuration = Duration.ZERO;

            for (LessonRequest lesson : section.getLessons()) {
                MultipartFile multipartFile = files.get("file_" + lesson.getName());

                storageService.validateVideo(multipartFile);
                File video = storageService.create(multipartFile, FileType.COURSE_VIDEO);

                Lesson currentLesson = new Lesson();
                currentLesson.setName(lesson.getName());
                currentLesson.setVideo(video);
                currentLesson.setSection(currentSection);

                sectionDuration = sectionDuration.plus(video.getDuration());

                currentSection.getLessons().add(currentLesson);
            }

            currentSection.setDuration(sectionDuration);
            courseDuration = courseDuration.plus(sectionDuration);

            course.getSections().add(currentSection);
        }

        course.setDuration(courseDuration);

        return courseMapper.toDto(courseQueryService.save(course));
    }

    public Page<CoursePublicResponse> getAllPublic(Pageable pageable) {
        Page<Course> courses = courseQueryService.findAll(pageable);

        return courses.map(courseMapper::toPublicDto);
    }

    public CourseResponse get(long courseId) {
        Course course = courseQueryService.findById(courseId);

        return courseMapper.toDto(course);
    }

    public List<Long> getLessonsIds(long courseId) {
        return courseQueryService.findAllLessonIdsById(courseId);
    }

    public CourseResponse update(long courseId, CourseRequest request, Map<String, MultipartFile> files) {
        Course course = courseQueryService.findById(courseId);

        User instructor = new User();
        instructor.setId(request.getInstructorId());

        Category category = new Category();
        category.setId(request.getCategoryId());

        course.setInstructor(instructor);
        course.setCategory(category);
        course.setTitle(request.getTitle());
        course.setSubtitle(request.getSubtitle());
        course.setDescription(request.getDescription());
        course.setRequirements(request.getRequirements());
        course.setAccess(request.getAccess());
        course.setLevel(request.getLevel());

        List<String> filenamesToDelete = new ArrayList<>();

        MultipartFile thumbPart = files.get("thumbnail");
        if (Objects.nonNull(thumbPart) && !thumbPart.isEmpty()) {
            filenamesToDelete.add(course.getThumbnail().getFilename());

            File thumbnail = storageService.create(thumbPart, FileType.THUMBNAIL);
            course.setThumbnail(thumbnail);
        }

        for (Section section : course.getSections()) {
            for (Lesson lesson : section.getLessons()) {
                File oldFile = lesson.getVideo();
                if (Objects.nonNull(oldFile) && !oldFile.isPrimary()) {
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
                    .lessons(new LinkedHashSet<>())
                    .build();

            for (LessonRequest lessonDto : sectionDto.getLessons()) {
                MultipartFile multipartFile = files.get("file_" + lessonDto.getName());

                storageService.validateVideo(multipartFile);

                File video = (Objects.nonNull(multipartFile) && !multipartFile.isEmpty())
                        ? storageService.create(multipartFile, FileType.COURSE_VIDEO)
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

        Course updated = courseQueryService.save(course);

        for (String filename : filenamesToDelete)
            storageService.get(filename);

        return courseMapper.toDto(updated);
    }

    public void delete(long courseId) {
        courseQueryService.deleteById(courseId);
    }

}

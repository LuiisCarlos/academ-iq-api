package dev.luiiscarlos.academ_iq_api.features.learning.course.facade;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.features.learning.course.service.CourseService;
import dev.luiiscarlos.academ_iq_api.shared.security.AdminContext;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CoursePublicResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.learning.course.service.CourseAdminService;
import dev.luiiscarlos.academ_iq_api.features.learning.course.service.CourseCrudService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseFacade {

    private final CourseAdminService courseAdminService;

    private final CourseService courseService;

    private final CourseCrudService courseCrudService;

    private final AdminContext adminContext;

    public CourseResponse create(CourseRequest request, Map<String, MultipartFile> files) {
        if (adminContext.isAdmin())
            log.warn("Admin '{}' created course with name {}", adminContext.admin(), request.getTitle());

        return courseAdminService.create(request, files);
    }

    public List<Long> getLessonsIds(long courseId) {
        if (adminContext.isAdmin())
            log.warn("Admin '{}' retrieved all lessons IDs for course with ID {}", adminContext.admin(), courseId);

        return courseAdminService.getLessonsIds(courseId);
    }

    public CourseResponse update(long courseId, CourseRequest request, Map<String, MultipartFile> files) {
        if (adminContext.isAdmin())
            log.warn("Admin '{}' updated course with ID {}", adminContext.admin(), courseId);

        return courseAdminService.update(courseId, request, files);
    }

    public void delete(long courseId) {
        if (adminContext.isAdmin())
            log.warn("Admin '{}' deleted course with ID {}", adminContext.admin(), courseId);

        courseAdminService.delete(courseId);
    }

    public Page<CoursePublicResponse> getAllPublic(Pageable pageable) {
        if (adminContext.isAdmin())
            log.info("Admin '{}' retrieved all users with pageable: {}", adminContext.admin(), pageable);

        return courseService.getAllPublic(pageable);
    }

    public CourseResponse get(long courseId) {
        if (adminContext.isAdmin())
            log.info("Admin '{}' retrieved course with ID {}", adminContext.admin(), courseId);

        return courseService.get(courseId);
    }

    public Course save(Course course) {
        return courseCrudService.save(course);
    }

    public Page<Course> findAll(Pageable pageable) {
        return courseCrudService.findAll(pageable);

    }

    public Course findById(long courseId) {
        return courseCrudService.findById(courseId);
    }

    public List<Long> findAllLessonIdsById(long courseId) {
        return courseCrudService.findAllLessonIdsById(courseId);
    }

    public void deleteById(long courseId) {
        courseCrudService.deleteById(courseId);
    }

    public Course getReferenceById(long courseId) {
        return courseCrudService.getReferenceById(courseId);
    }

    public boolean existsById(long courseId) {
        return courseCrudService.existsById(courseId);
    }

    public boolean existsByTitle(String title) {
        return courseCrudService.existsByTitle(title);
    }

}

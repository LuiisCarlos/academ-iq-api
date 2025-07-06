package dev.luiiscarlos.academ_iq_api.features.learning.course.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.features.identity.user.exception.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.learning.course.exception.CourseNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.learning.course.repository.CourseRepository;
import dev.luiiscarlos.academ_iq_api.features.learning.course.service.CourseCrudService;
import dev.luiiscarlos.academ_iq_api.shared.exception.ErrorMessages;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseCrudServiceImpl implements CourseCrudService {

    private final CourseRepository courseRepository;

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public Page<Course> findAll(Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(pageable);

        if (Objects.isNull(courses) || courses.isEmpty())
            throw new CourseNotFoundException(ErrorMessages.NO_COURSES_FOUND);

        return courses;
    }

    public Course findById(long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(String.format(ErrorMessages.COURSE_NOT_FOUND, courseId)));
    }

    public List<Long> findAllLessonIdsById(long courseId) {
        List<Long> lessonsIds = courseRepository.findAllLessonIdsById(courseId);

        if (Objects.isNull(lessonsIds) || lessonsIds.isEmpty())
            throw new CourseNotFoundException(String.format(ErrorMessages.COURSE_LESSONS_IDS_NOT_FOUND, courseId));

        return lessonsIds;
    }

    public void deleteById(long courseId) {
        courseRepository.findById(courseId).ifPresentOrElse((u) -> {
            courseRepository.deleteById(u.getId());
        }, () -> {
            throw new UserNotFoundException(String.format(ErrorMessages.COURSE_NOT_FOUND, courseId));
        });
    }

    public Course getReferenceById(long courseId) {
        return courseRepository.getReferenceById(courseId);
    }

    public boolean existsById(long courseId) {
        return courseRepository.existsById(courseId);
    }

    public boolean existsByTitle(String title) {
        return courseRepository.existsByTitle(title);
    }

}

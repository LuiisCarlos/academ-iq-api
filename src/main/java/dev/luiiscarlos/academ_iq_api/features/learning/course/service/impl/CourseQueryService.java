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
import dev.luiiscarlos.academ_iq_api.shared.exception.ErrorMessages;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseQueryService {

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

    public Course findById(long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(String.format(ErrorMessages.COURSE_NOT_FOUND, id)));
    }

    public List<Long> findAllLessonIdsById(Long id) {
        List<Long> lessonsIds = courseRepository.findAllLessonIdsById(id);

        if (Objects.isNull(lessonsIds) || lessonsIds.isEmpty())
            throw new CourseNotFoundException(String.format(ErrorMessages.COURSE_LESSONS_IDS_NOT_FOUND, id));

        return lessonsIds;
    }

    public void deleteById(Long id) {
        courseRepository.findById(id).ifPresentOrElse((u) -> {
            courseRepository.deleteById(u.getId());
        }, () -> {
            throw new UserNotFoundException(String.format(ErrorMessages.COURSE_NOT_FOUND, id));
        });
    }

    public boolean existsById(Long id) {
        return courseRepository.existsById(id);
    }

    public boolean existsByTitle(String title) {
        return courseRepository.existsByTitle(title);
    }

}

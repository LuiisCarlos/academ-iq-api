package dev.luiiscarlos.academ_iq_api.features.learning.course.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CoursePublicResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.mapper.CourseMapper;
import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.learning.course.service.CourseCrudService;
import dev.luiiscarlos.academ_iq_api.features.learning.course.service.CourseService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseCrudService courseQueryService;

    private final CourseMapper courseMapper;

    public Page<CoursePublicResponse> getAllPublic(Pageable pageable) {
        Page<Course> courses = courseQueryService.findAll(pageable);

        return courses.map(courseMapper::toPublicDto);
    }

    public CourseResponse get(long courseId) {
        Course course = courseQueryService.findById(courseId);

        return courseMapper.toDto(course);
    }

}

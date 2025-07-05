package dev.luiiscarlos.academ_iq_api.features.learning.course.facade;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.features.learning.course.service.CourseService;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CoursePublicResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.service.CourseAdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseFacade {

    private final CourseService courseService;

    private final CourseAdminService courseAdminService;

    public CourseResponse create(CourseRequest request, Map<String, MultipartFile> files) {
        return courseAdminService.create(request, files);
    }

    public List<Long> getLessonsIds(long courseId) {
        return courseAdminService.getLessonsIds(courseId);
    }

    public Page<CoursePublicResponse> getAllPublic(Pageable pageable) {
        return courseService.getAllPublic(pageable);
    }

    public CourseResponse get(long courseId) {
        return courseService.get(courseId);
    }

    public CourseResponse update(long courseId, CourseRequest request, Map<String, MultipartFile> files) {
        return courseAdminService.update(courseId, request, files);
    }

    public void delete(long courseId) {
        courseAdminService.delete(courseId);
    }

}

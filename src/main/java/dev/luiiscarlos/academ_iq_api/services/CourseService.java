package dev.luiiscarlos.academ_iq_api.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.exceptions.CourseNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.FileStorageException;
import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.dtos.CourseRequestResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.CourseMapper;
import dev.luiiscarlos.academ_iq_api.repositories.CourseRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    private final FileService fileService;

    public List<Course> findAll() {
        List<Course> courses = courseRepository.findAll();

        if (courses.isEmpty())
            throw new CourseNotFoundException("Failed to find courses: No courses found");

        return courses;
    }

    public Course findById(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new CourseNotFoundException("Failed to find course: Course not found with id " + id));
    }

    public CourseRequestResponseDto save(CourseRequestResponseDto RequestResponse) {
        Course course = courseMapper.toCourse(RequestResponse);

        courseRepository.save(course);

        return courseMapper.toCourseRequestResponseDto(course);
    }

    public CourseRequestResponseDto save(CourseRequestResponseDto RequestResponse,
    MultipartFile video) {

        if (video.isEmpty()) throw new FileStorageException("Failed to save course: Video is required");

        String videoUrl = fileService.save(video).getUrl();

        Course course = courseMapper.toCourse(RequestResponse, videoUrl);

        courseRepository.save(course);

        return courseMapper.toCourseRequestResponseDto(course);
    }

    public CourseRequestResponseDto save(CourseRequestResponseDto RequestResponse,
             MultipartFile thumbnail,
             MultipartFile video) {

        if (thumbnail.isEmpty()) throw new FileStorageException("Failed to save course: Thumbnail is required");
        if (video.isEmpty()) throw new FileStorageException("Failed to save course: Video is required");

        String thumbnailUrl = fileService.save(thumbnail).getUrl();
        String videoUrl = fileService.save(video).getUrl();

        Course course = courseMapper.toCourse(RequestResponse, thumbnailUrl, videoUrl);

        courseRepository.save(course);

        return courseMapper.toCourseRequestResponseDto(course);
    }

}

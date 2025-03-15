package dev.luiiscarlos.academ_iq_api.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.exceptions.CourseNotFoundException;
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

    private final FileServiceImpl fileService;

    /**
     * Find all courses
     *
     * @return the list of the courses
     */
    public List<Course> findAll() {
        List<Course> courses = courseRepository.findAll();

        if (courses.isEmpty())
            throw new CourseNotFoundException("Failed to find courses: No courses found");

        return courses;
    }

    /**
     * Finds the course by its id
     *
     * @param id the id of the course
     * @return the course
     */
    public Course findById(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new CourseNotFoundException("Failed to find course: Course not found with id " + id));
    }

    /**
     * Saves the course
     *
     * @param RequestResponse the course infomation
     * @param thumbnail the thumbnail of the course
     * @param video the video of the course
     * @return the course
     */
    public CourseRequestResponseDto save(CourseRequestResponseDto RequestResponse,
             MultipartFile thumbnail,
             MultipartFile video) {
        String thumbnailUrl = "";
        String videoUrl = "";

        if (!thumbnail.isEmpty() && thumbnail != null)
            thumbnailUrl = fileService.save(thumbnail, true).getUrl();

        if (!video.isEmpty() && thumbnail != null)
            videoUrl = fileService.save(video, false).getUrl();

        Course course = courseMapper.toCourse(RequestResponse, thumbnailUrl, videoUrl);
        courseRepository.save(course);

        return courseMapper.toCourseRequestResponseDto(course);
    }

    /**
     * Updates the course by its id
     *
     * @param RequestResponse the course infomation
     * @param thumbnail the thumbnail of the course
     * @param video the video of the course
     * @return the course
     */
    public CourseRequestResponseDto updateById(Long id,
            CourseRequestResponseDto RequestResponse,
            MultipartFile thumbnail,
            MultipartFile video) {
        return courseRepository.findById(id).map(c -> {
            String thumbnailUrl = "";
            String videoUrl = "";
            if (!thumbnail.isEmpty() && thumbnail != null)
                thumbnailUrl = fileService.save(thumbnail, true).getUrl();

            if (!video.isEmpty() && thumbnail != null)
                videoUrl = fileService.save(video, false).getUrl();

            Course course = courseMapper.toCourse(RequestResponse, thumbnailUrl, videoUrl);
            courseRepository.save(course);

            return courseMapper.toCourseRequestResponseDto(course);
        }).orElseThrow(() -> new CourseNotFoundException("Failed to find a course: Course not found with id " + id));
    }

    /**
     * Deletes the course by its id
     *
     * @param id the id of the course
     */
    public void deleteById(Long id) {
        if (!courseRepository.existsById(id))
            throw new CourseNotFoundException("Failed to find a course: Course not found with id " + id);

        courseRepository.deleteById(id);
    }

}

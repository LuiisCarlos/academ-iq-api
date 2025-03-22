package dev.luiiscarlos.academ_iq_api.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.exceptions.CourseAlreadyExistsEception;
import dev.luiiscarlos.academ_iq_api.exceptions.CourseNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.FileNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.models.Level;
import dev.luiiscarlos.academ_iq_api.models.dtos.CourseRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.CourseResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.FileResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.CourseMapper;
import dev.luiiscarlos.academ_iq_api.models.mappers.FileMapper;
import dev.luiiscarlos.academ_iq_api.repositories.CourseRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final FileServiceImpl fileService;

    private final CourseMapper courseMapper;

    private final FileMapper fileMapper;

    /**
     * Saves the course
     *
     * @param courseRequest the course details
     * @return the course
     */
    public CourseResponseDto save(CourseRequestDto courseRequest) {
        if (courseRepository.existsByName(courseRequest.getName()))
            throw new CourseAlreadyExistsEception("Failed to save course: Course already exists");

        File file = fileService.findByFilename("default-course-thumbnail.jpg");
        Course course = courseRepository.save(courseMapper.toCourse(courseRequest, file));

        return courseMapper.toCourseResponseDto(course);
    }

    public Course save(Course course) {
        if (courseRepository == null)
            throw new CourseNotFoundException("Failed to save course: Course is null");

        return courseRepository.save(course);
    }

    /**
     * Find all courses
     *
     * @return the list of the courses
     */
    public List<CourseResponseDto> findAll() {
        List<Course> courses = courseRepository.findAll();

        if (courses.isEmpty())
            throw new CourseNotFoundException("Failed to find courses: No courses found");

        return courses.stream().map(courseMapper::toCourseResponseDto).toList();
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
     * Finds the course's thumbnail by its id
     *
     * @param id the course's id
     * @return the course's thumbnail
     */
    @SuppressWarnings("null") // * Already handled
    public FileResponseDto findThumbnailById(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new CourseNotFoundException("Failed to find course: Course not found with id " + id));

        File file = fileService.findByFilename(course.getThumbnail().getFilename());
        return fileMapper.toFileResponseDto(file);
    }

    /**
     * Updates the course by its id
     *
     * @param RequestResponse the course details
     * @return the course
     */
    public CourseResponseDto updateById(Long id, CourseRequestDto courseRequest) {
        Course course =  courseRepository.findById(id).map(c -> {
            c.setName(courseRequest.getName());
            c.setDescription(courseRequest.getDescription());
            c.setAuthor(courseRequest.getAuthor());
            c.setRecommendedRequirements(courseRequest.getRecommendedRequirements());
            c.setCategory(courseRequest.getCategory());
            c.setLevel(Level.valueOf(courseRequest.getLevel()));
            // TODO: Calcular duración

            return courseRepository.save(c);
        }).orElseThrow(() -> new CourseNotFoundException("Failed to find a course: Course not found with id " + id));

        return courseMapper.toCourseResponseDto(course);
    }

    /**
     * Updates the course's thumbnail by its id
     *
     * @param id the id of the course
     * @param thumbnail the new thumbnail
     * @return the updated course
     */
    @SuppressWarnings("null") // * Already handled
    public CourseResponseDto updateThumbnailById(Long id, MultipartFile thumbnail) {
        if (thumbnail.isEmpty())
        throw new FileNotFoundException("Failed to save file: Thumbnail not found");
        if (!fileService.validateImage(thumbnail))
            throw new InvalidFileTypeException("Failed to update thumbnail: Invalid file content type");

        Course course = courseRepository.findById(id).map(c -> {
            fileService.deleteByFilename(c.getThumbnail().getFilename());

            File file = fileService.save(thumbnail, true);
            file.setUpdatedAt(LocalDateTime.now());
            c.setThumbnail(file);

            return courseRepository.save(c);
        }).orElseThrow(() -> new CourseNotFoundException("Failed to find a course: Course not found with id " + id));

        return courseMapper.toCourseResponseDto(course);
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

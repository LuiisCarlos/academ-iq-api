package dev.luiiscarlos.academ_iq_api.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.exceptions.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.exceptions.SectionNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.FileNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.models.Section;
import dev.luiiscarlos.academ_iq_api.models.dtos.CourseResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.SectionResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.CourseMapper;
import dev.luiiscarlos.academ_iq_api.models.mappers.SectionMapper;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SectionService {

    private final SectionMapper sectionMapper;

    private final CourseService courseService;

    private final CourseMapper courseMapper;

    private final FileServiceImpl fileService;

    /**
     * Saves the section by the course's id
     *
     * @param courseId the course's id
     * @param sectionName the section's name
     * @param videos the section's videos
     * @return the course
     */
    @SuppressWarnings("null") // Already handled
    public CourseResponseDto save(Long courseId, String sectionName, MultipartFile ...videos) {
        Course course = courseService.findById(courseId);
        List<File> files = new ArrayList<>();

        if (videos.length == 0 || videos == null)
            throw new SectionNotFoundException("Failed to create section: No videos found");

        for (MultipartFile video : videos) {
            if (video.isEmpty())
                throw new FileNotFoundException("Failed to create section: Empty video file " + video.getOriginalFilename());

            if (!fileService.validateVideo(video) || fileService.validateImage(video))
                throw new InvalidFileTypeException(
                    "Failed to create section: Invalid video content type " + video.getContentType());

            files.add(fileService.save(video, false));
        }

        Section section = Section.builder()
            .name(sectionName)
            .course(course)
            .videos(files)
            .build();

        course.getSections().add(section);

        return courseMapper.toCourseResponseDto(courseService.save(course));
    }

    /**
     * Finds all the sections
     *
     * @return the sections
     */
    @SuppressWarnings("null") // Already handled
    public List<SectionResponseDto> findAll(Long courseId) {
        Course course = courseService.findById(courseId);
        return course.getSections().stream()
            .map(sectionMapper::toSectionResponseDto)
            .toList();
    }

    /**
     * Finds the section by the course's id and the section's id
     *
     * @param courseId the course's id
     * @param sectionId the section's id
     * @return the section
     */
    @SuppressWarnings("null") // Already handled
    public SectionResponseDto findById(Long courseId, int sectionIndex) {
        Course course = courseService.findById(courseId);

        try {
            Section section = course.getSections().get(sectionIndex);
            return sectionMapper.toSectionResponseDto(section);
        } catch (IndexOutOfBoundsException e) {
            throw new SectionNotFoundException("Failed to find section: Section not found with index " + sectionIndex);
        }
    }

    /**
     * Updates the section by the course's id and the section's id
     *
     * @param courseId the course's id
     * @param sectionId the section's id
     * @param section the section
     * @return the section
     */
    @SuppressWarnings("null") // Already handled
    public SectionResponseDto updateById(Long courseId, int sectionIndex, String sectionName, MultipartFile ...videos) { // TODO: When updating with empty videos, it should not delete the previous videos
        Course course = courseService.findById(courseId);
        Section section = null;
        try {
            section = course.getSections().get(sectionIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new SectionNotFoundException("Failed to update section: Section not found with index " + sectionIndex);
        }

        section.setName(sectionName);
        section.getVideos().forEach(file -> fileService.deleteByFilename(file.getFilename()));
        section.getVideos().clear();

        if (videos.length == 0 || videos == null)
            throw new SectionNotFoundException("Failed to update section: No videos found");

        for (MultipartFile video : videos) {
            if (video.isEmpty())
                throw new FileNotFoundException("Failed to update section: Empty video file " + video.getOriginalFilename());

            if (!fileService.validateVideo(video) || fileService.validateImage(video))
                throw new InvalidFileTypeException(
                    "Failed to update section: Invalid video content type " + video.getContentType());


            section.getVideos().add(fileService.save(video, false));
        }

        courseService.save(course);

        return sectionMapper.toSectionResponseDto(section);
    }

    /**
     * Deletes the section by the course's id and the section's id
     *
     * @param courseId the course's id
     * @param sectionId the section's id
     */
    @SuppressWarnings("null") // Already handled
    public void deleteById(Long courseId, int sectionIndex) {
        Course course = courseService.findById(courseId);
        Section section = null;

        try {
            section = course.getSections().get(sectionIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new SectionNotFoundException("Failed to update section: Section not found with index " + sectionIndex);
        }

        section.getVideos().forEach(file -> { // TODO: Only deletes one file from the database
            System.out.println("\nNombre del archivo eliminado: " + file.getFilename() + "\n");
            fileService.deleteByFilename(file.getFilename());
        });
        section.getVideos().clear();

        course.getSections().remove(sectionIndex);
        courseService.save(course);
    }

}

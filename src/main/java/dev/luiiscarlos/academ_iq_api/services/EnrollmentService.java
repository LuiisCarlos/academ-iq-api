package dev.luiiscarlos.academ_iq_api.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.luiiscarlos.academ_iq_api.models.dtos.course.CourseProgressDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.enrollment.EnrollmentResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.enrollment.EnrollmentUpdateDto;
import dev.luiiscarlos.academ_iq_api.exceptions.EnrollmentNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.Enrollment;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.mappers.EnrollmentMapper;
import dev.luiiscarlos.academ_iq_api.repositories.EnrollmentRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    private final EnrollmentMapper enrollmentMapper;

    private final UserServiceImpl userService;

    private final CourseService courseService;

    private final ObjectMapper objectMapper;

    /**
     * Creates a new enrollment for a user in a specific course
     *
     * @param token    the authentication token of the user
     * @param courseId the ID of the course
     * @param flags    a map containing optional enrollment flags
     *
     * @return an {@link EnrollmentResponseDto} containing the newly created
     *         enrollment
     *
     * @throws EnrollmentNotFoundException if the user is already enrolled in the
     *                                     course
     */
    public EnrollmentResponseDto save(String token, Long courseId, Map<String, Boolean> flags) {
        User user = userService.findByToken(token);
        Course course = courseService.findById(courseId);

        if (enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId))
            throw new EnrollmentNotFoundException(
                    "Failed to save enrollment: User is already enrolled");

        Enrollment enrollment = enrollmentMapper.toEnrollment(user, course);

        if (flags != null)
            enrollment.setIsFavorite(flags.getOrDefault("isFavorite", false));

        enrollmentRepository.save(enrollment);

        return enrollmentMapper.toEnrollmentResponse(enrollment);
    }

    /**
     * Retrieves the list of enrollments for a specific user
     *
     * @param token the authentication token of the user
     *
     * @return an {@link EnrollmentResponseDto} containing enrollment data
     *
     * @throws EnrollmentNotFoundException if no enrollments found for the given
     *                                     user
     */
    public List<EnrollmentResponseDto> findAllByUserId(String token) {
        User user = userService.findByToken(token);
        List<Enrollment> enrollments = enrollmentRepository.findAllByUserId(user.getId());

        if (enrollments.isEmpty() || enrollments == null)
            throw new EnrollmentNotFoundException("Failed to find enrollments: No enrollments found");

        return enrollments.stream()
                .map(enrollmentMapper::toEnrollmentResponse)
                .toList();
    }

    /**
     * Retrieves the enrollment information for a specific user and course
     *
     * @param token    the authentication token of the user
     * @param courseId the ID of the course
     *
     * @return an {@link EnrollmentResponseDto} containing enrollment data
     *
     * @throws EnrollmentNotFoundException if no enrollment is found for the given
     *                                     user and course
     */
    public EnrollmentResponseDto findByUserIdAndCourseId(String token, Long courseId) {
        User user = userService.findByToken(token);

        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Failed to find enrollment: Enrollment not found with course id: " + courseId));

        return enrollmentMapper.toEnrollmentResponse(enrollment);
    }

    /**
     * Updates the enrollment for a specific user and course
     *
     * @param token         the authentication token of the user
     * @param courseId      the ID of the course
     * @param enrollmentDto the updated enrollment data
     *
     * @return an {@link EnrollmentResponseDto} containing the updated enrollment
     *         information
     *
     * @throws EnrollmentNotFoundException if no enrollment is found for the given
     *                                     user and course
     */
    public EnrollmentResponseDto updateByUserIdAndCourseId(String token, Long courseId,
            EnrollmentUpdateDto enrollmentDto) {
        User user = userService.findByToken(token);
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Failed to update enrollment: Enrollment not found with course id " + courseId));

        // enrollment.setProgress(enrollmentDto.getProgress()); // TODO: Review this
        enrollment.setIsFavorite(enrollmentDto.isFavorite());
        enrollment.setIsArchived(enrollmentDto.isArchived());

        if (!enrollment.isCompleted() && enrollmentDto.isCompleted()) { // TODO: Review this
            // user.setHours(user.getHours() +
            // enrollment.getCourse().getDuration().getHour());
            // userService.updateHours();
            enrollment.setCompletedAt(LocalDateTime.now());
            enrollment.setIsCompleted(enrollmentDto.isCompleted());
        }

        return enrollmentMapper.toEnrollmentResponse(enrollment);
    }

    /**
     *
     * @param token
     * @param courseId
     * @param progressDto
     *
     * @return
     */
    public EnrollmentResponseDto updateProgressByUserIdAndCourseId(
            String token,
            Long courseId,
            CourseProgressDto progressDto) {
        User user = userService.findByToken(token);
        log.debug(progressDto.toString());
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(user.getId(), courseId).map(e -> {
            try {
                String progressJson = objectMapper.writeValueAsString(progressDto);
                e.setProgressState(progressJson);
                return enrollmentRepository.save(e);
            } catch (JsonProcessingException ex) {
                throw new IllegalStateException("Error al serializar el estado de progreso", ex);
            }
        }).orElseThrow(() -> new EnrollmentNotFoundException(
            "Failed to update enrollment: Enrollment not found with course id " + courseId));

        return enrollmentMapper.toEnrollmentResponse(enrollment);
    }

    /**
     * Partially updates enrollment properties (isFavorite, isArchived, isCompleted)
     * for a specific user and course.
     *
     * @param token    the authentication token of the user
     * @param courseId the ID of the course
     * @param updates  a map containing the fields to update and their new values
     *
     * @throws EnrollmentNotFoundException if no enrollment is found for the given
     *                                     user and course
     */
    public EnrollmentResponseDto patchByUserIdAndCourseId(String token, Long courseId, Map<String, Boolean> updates) {
        User user = userService.findByToken(token);
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Failed to update enrollment: Enrollment not found with course id " + courseId));

        if (updates.containsKey("isFavorite"))
            enrollment.setIsFavorite(updates.get("isFavorite"));

        if (updates.containsKey("isArchived"))
            enrollment.setIsArchived(updates.get("isArchived"));

        if (updates.containsKey("isCompleted"))
            enrollment.setIsCompleted(updates.get("isCompleted"));

        return enrollmentMapper.toEnrollmentResponse(enrollmentRepository.save(enrollment));
    }

    /**
     * Deletes the enrollment for a specific user and course
     *
     * @param token    the authentication token of the user
     * @param courseId the ID of the course
     *
     * @throws EnrollmentNotFoundException if no enrollment is found for the given
     *                                     user and course
     */
    public void deleteByUserIdAndCourseId(String token, Long courseId) {
        User user = userService.findByToken(token);

        if (!enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId))
            throw new EnrollmentNotFoundException(
                    "Failed to delete enrollment: Enrollment no found with course id " + courseId);

        enrollmentRepository.deleteByUserIdAndCourseId(user.getId(), courseId);
    }

    /*
     * public void updateLessonProgress(
     * Long enrollmentId,
     * Long sectionId,
     * Long lessonId,
     * boolean isCompleted,
     * double videoProgress) {
     * Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
     * .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found"));
     *
     * CourseProgressDto progress;
     *
     * try {
     * progress = objectMapper.readValue(enrollment.getProgress(),
     * CourseProgressDto.class);
     * } catch (JsonProcessingException e) {
     * progress = initializeNewProgress();
     * }
     *
     * // Buscar o crear la sección
     * SectionProgressDto section = progress.getSections().stream()
     * .filter(s -> s.getSectionId().equals(sectionId))
     * .findFirst()
     * .orElseGet(() -> {
     * SectionProgressDto newSection = new SectionProgressDto();
     * newSection.setSectionId(sectionId);
     * newSection.setLessons(new ArrayList<>());
     * progress.getSections().add(newSection);
     * return newSection;
     * });
     *
     * // Buscar o crear la lección
     * LessonProgressDto lesson = section.getLessons().stream()
     * .filter(l -> l.getLessonId().equals(lessonId))
     * .findFirst()
     * .orElseGet(() -> {
     * LessonProgressDto newLesson = new LessonProgressDto();
     * newLesson.setLessonId(lessonId);
     * section.getLessons().add(newLesson);
     * return newLesson;
     * });
     *
     * // Actualizar datos de la lección
     * lesson.setCompleted(isCompleted);
     * lesson.setVideoProgress(videoProgress);
     * lesson.setLastAccessed(LocalDateTime.now());
     *
     * // Verificar si todas las lecciones están completadas
     * section.setCompleted(section.getLessons().stream().allMatch(LessonProgressDto
     * ::isCompleted));
     *
     * // Guardar el progreso actualizado
     * try {
     * enrollment.setProgress(objectMapper.writeValueAsString(progress));
     * enrollmentRepository.save(enrollment);
     * } catch (JsonProcessingException e) {
     * throw new IllegalStateException("Failed to serialize progress", e);
     * }
     * }
     *
     * private CourseProgressDto initializeNewProgress() {
     * CourseProgressDto progress = new CourseProgressDto();
     * progress.setSections(new ArrayList<>());
     * return progress;
     * }
     */

}

package br.com.alura.AluraFake.domain.user.service;

import br.com.alura.AluraFake.core.exception.type.BadRequestException;
import br.com.alura.AluraFake.core.exception.type.BusinessRulesException;
import br.com.alura.AluraFake.core.exception.type.ResourceNotFoundException;
import br.com.alura.AluraFake.domain.course.dto.CourseSummaryResponse;
import br.com.alura.AluraFake.domain.course.model.Course;
import br.com.alura.AluraFake.domain.course.model.Status;
import br.com.alura.AluraFake.domain.course.repository.CourseRepository;
import br.com.alura.AluraFake.domain.task.repository.TaskRepository;
import br.com.alura.AluraFake.domain.user.dto.InstructorCoursesResponse;
import br.com.alura.AluraFake.domain.user.model.Role;
import br.com.alura.AluraFake.domain.user.model.User;
import br.com.alura.AluraFake.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InstructorService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;

    public InstructorService(UserRepository userRepository, CourseRepository courseRepository, TaskRepository taskRepository){
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.taskRepository = taskRepository;
    }

    public InstructorCoursesResponse getInstructorCourses(Long instructorId) {
        User user = this.userRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id " + instructorId));

        if(!user.getRole().equals(Role.INSTRUCTOR)) {
            throw new BadRequestException("The user is not an instructor.");
        }

        List<Course> courses = this.courseRepository.findByInstructorId(instructorId);
        if (courses.isEmpty()) {
            return new InstructorCoursesResponse(Collections.emptyList(), 0);
        }

        long totalPublishedCourses = 0;
        List<Long> courseIds = new ArrayList<>();
        for (Course course : courses) {
            courseIds.add(course.getId());
            if(course.getStatus().equals(Status.PUBLISHED)) {
                totalPublishedCourses++;
            }
        }

        Map<Long, Long> courseTotalTasks = new HashMap<>();
        List<Map<String, Object>> results = taskRepository.countTasksByCourseIds(courseIds);
        for (Map<String, Object> result : results) {
            Long courseId = (Long) result.get("courseId");
            Long totalTasks = (Long) result.get("totalTasks");
            courseTotalTasks.put(courseId, totalTasks);
        }

        List<CourseSummaryResponse> courseSummaries = new ArrayList<>();
        for (Course course : courses) {
            CourseSummaryResponse courseSummaryResponse =
                    new CourseSummaryResponse(
                            course.getId(),
                            course.getTitle(),
                            course.getStatus(),
                            course.getPublishedAt(),
                            courseTotalTasks.containsKey(course.getId()) ? courseTotalTasks.get(course.getId()) : 0);
            courseSummaries.add(courseSummaryResponse);
        }

        return new InstructorCoursesResponse(courseSummaries, totalPublishedCourses);
    }

}

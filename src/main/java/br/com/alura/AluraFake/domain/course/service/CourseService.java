package br.com.alura.AluraFake.domain.course.service;

import br.com.alura.AluraFake.core.exception.type.BusinessRulesException;
import br.com.alura.AluraFake.core.exception.type.ResourceNotFoundException;
import br.com.alura.AluraFake.domain.course.dto.PublishedCourseResponse;
import br.com.alura.AluraFake.domain.course.model.Course;
import br.com.alura.AluraFake.domain.course.model.Status;
import br.com.alura.AluraFake.domain.course.repository.CourseRepository;
import br.com.alura.AluraFake.domain.task.model.Task;
import br.com.alura.AluraFake.domain.task.model.Type;
import br.com.alura.AluraFake.domain.task.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;

    public CourseService(CourseRepository courseRepository, TaskRepository taskRepository) {
        this.courseRepository = courseRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public PublishedCourseResponse publish(Long courseId) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId));

        if (!Status.BUILDING.name().equals(course.getStatus().name())) {
            throw new BusinessRulesException("Course must be in BUILDING status to publish");
        }

        int order = 1;
        List<Task> tasks = taskRepository.findByCourseIdAndOrderGreaterThanEqualOrderByOrderAsc(courseId, order);
        if(tasks.isEmpty()) {
            throw new BusinessRulesException("The course must contain at least one activity of each type.");
        }

        Set<Type> taskTypes = new HashSet<>();
        for (Task task : tasks) {
            if(task.getOrder() != order) {
                throw new BusinessRulesException("The course should include activities with a continuous sequential order.");
            }
            order++;
            taskTypes.add(task.getType());
        }

        if(taskTypes.size() != Type.values().length) {
            throw new BusinessRulesException("The course must contain at least one activity of each type.");
        }

        course.setStatus(Status.PUBLISHED);
        course.setPublishedAt(LocalDateTime.now());

        this.courseRepository.save(course);
        return PublishedCourseResponse.from(course);
    }

}

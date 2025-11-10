package br.com.alura.AluraFake.domain.task.service;

import br.com.alura.AluraFake.core.exception.type.BadRequestException;
import br.com.alura.AluraFake.core.exception.type.BusinessRulesException;
import br.com.alura.AluraFake.core.exception.type.ResourceNotFoundException;
import br.com.alura.AluraFake.domain.course.model.Course;
import br.com.alura.AluraFake.domain.course.model.Status;
import br.com.alura.AluraFake.domain.course.repository.CourseRepository;
import br.com.alura.AluraFake.domain.task.dto.OpenTextTaskRequest;
import br.com.alura.AluraFake.domain.task.dto.OpenTextTaskResponse;
import br.com.alura.AluraFake.domain.task.model.OpenTextTask;
import br.com.alura.AluraFake.domain.task.model.Task;
import br.com.alura.AluraFake.domain.task.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CourseRepository courseRepository;

    public TaskService(TaskRepository taskRepository, CourseRepository courseRepository) {
        this.taskRepository = taskRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public OpenTextTaskResponse createOpenTextTask(OpenTextTaskRequest request) {
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + request.courseId()));

        validateGeneralRules(request.statement(), request.order(), course);

        OpenTextTask task = new OpenTextTask(request.statement(), request.order(), course);
        OpenTextTask taskSaved = taskRepository.save(task);
        return OpenTextTaskResponse.from(taskSaved);
    }

    private void validateGeneralRules(String statement, int order, Course course) {
        if (!Status.BUILDING.name().equals(course.getStatus().name())) {
            throw new BusinessRulesException("Course must be in BUILDING status to add tasks");
        }

        if (this.taskRepository.existsByCourseIdAndStatement(course.getId(), statement)) {
            throw new BadRequestException("Course already has a task with this statement");
        }

        int currentTaskCount = this.taskRepository.countByCourseId(course.getId());
        if (order > currentTaskCount + 1) {
            throw new BadRequestException("Invalid order: there are missing previous orders");
        }

        List<Task> tasksToShift = this.taskRepository.findByCourseIdAndOrderGreaterThanEqualOrderByOrderAsc(course.getId(), order);
        if(!tasksToShift.isEmpty()) {
            for (Task task : tasksToShift) {
                task.setOrder(task.getOrder()+1);
            }
            this.taskRepository.saveAll(tasksToShift);
        }
    }

}

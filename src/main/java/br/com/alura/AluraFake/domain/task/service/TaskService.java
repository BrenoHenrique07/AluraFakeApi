package br.com.alura.AluraFake.domain.task.service;

import br.com.alura.AluraFake.core.exception.type.BadRequestException;
import br.com.alura.AluraFake.core.exception.type.BusinessRulesException;
import br.com.alura.AluraFake.core.exception.type.ResourceNotFoundException;
import br.com.alura.AluraFake.domain.course.model.Course;
import br.com.alura.AluraFake.domain.course.model.Status;
import br.com.alura.AluraFake.domain.course.repository.CourseRepository;
import br.com.alura.AluraFake.domain.task.dto.opentext.OpenTextTaskRequest;
import br.com.alura.AluraFake.domain.task.dto.opentext.OpenTextTaskResponse;
import br.com.alura.AluraFake.domain.task.dto.singlechoice.SingleChoiceOptionRequest;
import br.com.alura.AluraFake.domain.task.dto.singlechoice.SingleChoiceTaskRequest;
import br.com.alura.AluraFake.domain.task.dto.singlechoice.SingleChoiceTaskResponse;
import br.com.alura.AluraFake.domain.task.model.OpenTextTask;
import br.com.alura.AluraFake.domain.task.model.SingleChoiceOption;
import br.com.alura.AluraFake.domain.task.model.SingleChoiceTask;
import br.com.alura.AluraFake.domain.task.model.Task;
import br.com.alura.AluraFake.domain.task.repository.SingleChoiceOptionRepository;
import br.com.alura.AluraFake.domain.task.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final SingleChoiceOptionRepository singleChoiceOptionRepository;
    private final CourseRepository courseRepository;

    public TaskService(TaskRepository taskRepository, SingleChoiceOptionRepository singleChoiceOptionRepository, CourseRepository courseRepository) {
        this.taskRepository = taskRepository;
        this.singleChoiceOptionRepository = singleChoiceOptionRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public OpenTextTaskResponse createOpenTextTask(OpenTextTaskRequest request) {
        Course course = findCourseOrThrow(request.courseId());
        validateGeneralRules(request.statement(), request.order(), course);

        OpenTextTask task = new OpenTextTask(request.statement(), request.order(), course);
        OpenTextTask taskSaved = this.taskRepository.save(task);
        return OpenTextTaskResponse.from(taskSaved);
    }

    @Transactional
    public SingleChoiceTaskResponse createSingleChoiceTask(SingleChoiceTaskRequest request) {
        Course course = findCourseOrThrow(request.courseId());
        validateSingleChoiceTask(request);
        validateGeneralRules(request.statement(), request.order(), course);

        SingleChoiceTask singleChoiceTask = new SingleChoiceTask(request.statement(), request.order(), course);
        SingleChoiceTask taskSaved = this.taskRepository.save(singleChoiceTask);

        List<SingleChoiceOption> options = mapOptions(request.options(), taskSaved);
        List<SingleChoiceOption> optionsSaved = this.singleChoiceOptionRepository.saveAll(options);

        return SingleChoiceTaskResponse.from(taskSaved, optionsSaved);
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

    private void validateSingleChoiceTask(SingleChoiceTaskRequest request) {
        Set<String> uniqueOptions = new HashSet<>();
        long correctOptionsCount = 0;

        for (SingleChoiceOptionRequest option : request.options()) {
            String optionText = option.option().strip();
            if (optionText.equalsIgnoreCase(request.statement())) {
                throw new BusinessRulesException("The option cannot be the same as the statement.");
            }
            if (!uniqueOptions.add(optionText.toLowerCase())) {
                throw new BusinessRulesException("The options cannot be identical to each other.");
            }
            if (option.isCorrect()) {
                correctOptionsCount++;
            }
        }
        if (correctOptionsCount != 1) {
            throw new BusinessRulesException("The task must have exactly one correct answer.");
        }
    }

    private List<SingleChoiceOption> mapOptions(List<SingleChoiceOptionRequest> optionsRequest, SingleChoiceTask task) {
        List<SingleChoiceOption> options = new ArrayList<>();
        for (SingleChoiceOptionRequest optionRequest : optionsRequest) {
            SingleChoiceOption option = new SingleChoiceOption(optionRequest.option(), optionRequest.isCorrect(), task);
            options.add(option);
        }
        return options;
    }

    private Course findCourseOrThrow(long courseId) {
        return this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId));
    }

}

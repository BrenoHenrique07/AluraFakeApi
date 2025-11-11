package br.com.alura.AluraFake.domain.task.controller;

import br.com.alura.AluraFake.domain.task.dto.multiplechoice.MultipleChoiceTaskRequest;
import br.com.alura.AluraFake.domain.task.dto.multiplechoice.MultipleChoiceTaskResponse;
import br.com.alura.AluraFake.domain.task.dto.opentext.OpenTextTaskRequest;
import br.com.alura.AluraFake.domain.task.dto.opentext.OpenTextTaskResponse;
import br.com.alura.AluraFake.domain.task.dto.singlechoice.SingleChoiceTaskRequest;
import br.com.alura.AluraFake.domain.task.dto.singlechoice.SingleChoiceTaskResponse;
import br.com.alura.AluraFake.domain.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/task/new/opentext")
    public ResponseEntity<OpenTextTaskResponse> newOpenTextExercise(@RequestBody @Valid OpenTextTaskRequest openTexTaskRequest) {
        OpenTextTaskResponse openTextTaskResponse = this.taskService.createOpenTextTask(openTexTaskRequest);
        return ResponseEntity.ok().body(openTextTaskResponse);
    }

    @PostMapping("/task/new/singlechoice")
    public ResponseEntity<SingleChoiceTaskResponse> newSingleChoice(@RequestBody @Valid SingleChoiceTaskRequest singleChoiceTaskRequest) {
        SingleChoiceTaskResponse singleChoiceTaskResponse = this.taskService.createSingleChoiceTask(singleChoiceTaskRequest);
        return ResponseEntity.ok().body(singleChoiceTaskResponse);
    }

    @PostMapping("/task/new/multiplechoice")
    public ResponseEntity<MultipleChoiceTaskResponse> newMultipleChoice(@RequestBody @Valid MultipleChoiceTaskRequest multipleChoiceTaskRequest) {
        MultipleChoiceTaskResponse multipleChoiceTaskResponse = this.taskService.createMultipleChoiceTask(multipleChoiceTaskRequest);
        return ResponseEntity.ok().body(multipleChoiceTaskResponse);
    }

}
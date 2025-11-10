package br.com.alura.AluraFake.domain.task.controller;

import br.com.alura.AluraFake.domain.task.dto.OpenTextTaskRequest;
import br.com.alura.AluraFake.domain.task.dto.OpenTextTaskResponse;
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
    public ResponseEntity newSingleChoice() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/task/new/multiplechoice")
    public ResponseEntity newMultipleChoice() {
        return ResponseEntity.ok().build();
    }

}
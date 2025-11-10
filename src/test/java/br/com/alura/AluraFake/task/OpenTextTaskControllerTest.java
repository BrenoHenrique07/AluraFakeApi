package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.core.exception.handler.GlobalExceptionHandler;
import br.com.alura.AluraFake.domain.course.model.Course;
import br.com.alura.AluraFake.domain.course.model.Status;
import br.com.alura.AluraFake.domain.course.repository.CourseRepository;
import br.com.alura.AluraFake.domain.task.controller.TaskController;
import br.com.alura.AluraFake.domain.task.dto.OpenTextTaskRequest;
import br.com.alura.AluraFake.domain.task.model.OpenTextTask;
import br.com.alura.AluraFake.domain.task.repository.TaskRepository;
import br.com.alura.AluraFake.domain.task.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import({TaskService.class, GlobalExceptionHandler.class})
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void newOpenTextExercise_should_return_404_when_course_not_found() throws Exception {
        OpenTextTaskRequest dto = new OpenTextTaskRequest(
                2L,
                "Questão 1",
                1
        );

        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_bad_request_when_statement_is_too_short() throws Exception {
        OpenTextTaskRequest dto = new OpenTextTaskRequest(1L, "abc", 1);

        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.BUILDING);

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_bad_request_when_order_is_not_positive() throws Exception {
        OpenTextTaskRequest dto = new OpenTextTaskRequest(1L, "Atividade válida", 0);

        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.BUILDING);

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_bad_request_when_course_is_not_building() throws Exception {
        OpenTextTaskRequest dto = new OpenTextTaskRequest(1L, "Atividade válida", 1);

        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.PUBLISHED);

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void should_return_bad_request_when_duplicate_statement_exists() throws Exception {
        OpenTextTaskRequest dto = new OpenTextTaskRequest(
                1L,
                "Atividade repetida",
                1
        );

        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.BUILDING);
        when(course.getId()).thenReturn(1L);

        when(taskRepository.existsByCourseIdAndStatement(1L, "Atividade repetida"))
                .thenReturn(true);

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

}

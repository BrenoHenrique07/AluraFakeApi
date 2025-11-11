package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.core.exception.handler.GlobalExceptionHandler;
import br.com.alura.AluraFake.domain.course.model.Course;
import br.com.alura.AluraFake.domain.course.model.Status;
import br.com.alura.AluraFake.domain.course.repository.CourseRepository;
import br.com.alura.AluraFake.domain.task.controller.TaskController;
import br.com.alura.AluraFake.domain.task.dto.singlechoice.SingleChoiceOptionRequest;
import br.com.alura.AluraFake.domain.task.dto.singlechoice.SingleChoiceTaskRequest;
import br.com.alura.AluraFake.domain.task.repository.SingleChoiceOptionRepository;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import({TaskService.class, GlobalExceptionHandler.class})
class SingleChoiceTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private SingleChoiceOptionRepository singleChoiceOptionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_bad_request_when_number_of_options_is_less_than_two_or_greater_than_five() throws Exception {
        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.BUILDING);

        var requestWithOneOption = new SingleChoiceTaskRequest(
                1L,
                "Capital do Brasil",
                1,
                List.of(new SingleChoiceOptionRequest("Brasília", true))
        );

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithOneOption)))
                .andExpect(status().isBadRequest());

        var requestWithSixOptions = new SingleChoiceTaskRequest(
                1L,
                "Capital do Brasil",
                1,
                List.of(
                        new SingleChoiceOptionRequest("Brasília", true),
                        new SingleChoiceOptionRequest("Rio de Janeiro", false),
                        new SingleChoiceOptionRequest("São Paulo", false),
                        new SingleChoiceOptionRequest("Belo Horizonte", false),
                        new SingleChoiceOptionRequest("Curitiba", false),
                        new SingleChoiceOptionRequest("Salvador", false)
                )
        );

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithSixOptions)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_bad_request_when_option_text_length_is_invalid() throws Exception {
        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.BUILDING);

        var requestWithShortOption = new SingleChoiceTaskRequest(
                1L,
                "Capital do Brasil",
                1,
                List.of(
                        new SingleChoiceOptionRequest("abc", true),
                        new SingleChoiceOptionRequest("Rio de Janeiro", false)
                )
        );

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithShortOption)))
                .andExpect(status().isBadRequest());

        String longText = "A".repeat(81);
        var requestWithLongOption = new SingleChoiceTaskRequest(
                1L,
                "Capital do Brasil",
                1,
                List.of(
                        new SingleChoiceOptionRequest(longText, true),
                        new SingleChoiceOptionRequest("Rio de Janeiro", false)
                )
        );

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithLongOption)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_bad_request_when_option_equals_statement() throws Exception {
        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.BUILDING);

        var request = new SingleChoiceTaskRequest(
                1L,
                "Capital do Brasil",
                1,
                List.of(new SingleChoiceOptionRequest("Capital do Brasil", false),
                        new SingleChoiceOptionRequest("Fortaleza", false))
        );

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void should_return_bad_request_when_more_than_one_correct_option() throws Exception {
        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.BUILDING);

        var request = new SingleChoiceTaskRequest(
                1L,
                "Capital do Brasil",
                1,
                List.of(
                        new SingleChoiceOptionRequest("Brasília", true),
                        new SingleChoiceOptionRequest("Rio de Janeiro", true)
                )
        );

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void should_return_bad_request_when_duplicate_options() throws Exception {
        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.BUILDING);

        var request = new SingleChoiceTaskRequest(
                1L,
                "Capital do Brasil",
                1,
        List.of(
                        new SingleChoiceOptionRequest("Brasília", false),
                        new SingleChoiceOptionRequest("Brasília", true)
                )
                        );

                        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnprocessableEntity());
    }
}

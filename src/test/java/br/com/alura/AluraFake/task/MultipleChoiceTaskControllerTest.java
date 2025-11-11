package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.core.exception.handler.GlobalExceptionHandler;
import br.com.alura.AluraFake.domain.course.model.Course;
import br.com.alura.AluraFake.domain.course.model.Status;
import br.com.alura.AluraFake.domain.course.repository.CourseRepository;
import br.com.alura.AluraFake.domain.task.controller.TaskController;
import br.com.alura.AluraFake.domain.task.dto.multiplechoice.MultipleChoiceOptionRequest;
import br.com.alura.AluraFake.domain.task.dto.multiplechoice.MultipleChoiceTaskRequest;
import br.com.alura.AluraFake.domain.task.repository.MultipleChoiceOptionRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import({TaskService.class, GlobalExceptionHandler.class})
class MultipleChoiceTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private SingleChoiceOptionRepository singleChoiceOptionRepository;

    @MockBean
    private MultipleChoiceOptionRepository multipleChoiceOptionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_bad_request_when_number_of_options_is_less_than_three_or_greater_than_five() throws Exception {
        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.BUILDING);

        var requestWithOneOption = new MultipleChoiceTaskRequest(
                1L,
                "Capital do Brasil",
                1,
                List.of(new MultipleChoiceOptionRequest("Brasília", true),
                        new MultipleChoiceOptionRequest("Rio de Janeiro", false)
                )
        );

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithOneOption)))
                .andExpect(status().isBadRequest());

        var requestWithSixOptions = new MultipleChoiceTaskRequest(
                1L,
                "Capital do Brasil",
                1,
                List.of(
                        new MultipleChoiceOptionRequest("Brasília", true),
                        new MultipleChoiceOptionRequest("Rio de Janeiro", false),
                        new MultipleChoiceOptionRequest("São Paulo", false),
                        new MultipleChoiceOptionRequest("Belo Horizonte", false),
                        new MultipleChoiceOptionRequest("Curitiba", false),
                        new MultipleChoiceOptionRequest("Salvador", false)
                )
        );

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithSixOptions)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_bad_request_when_option_text_length_is_invalid() throws Exception {
        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.BUILDING);

        var requestWithShortOption = new MultipleChoiceTaskRequest(
                1L,
                "Capital do Brasil",
                1,
                List.of(
                        new MultipleChoiceOptionRequest("abc", true),
                        new MultipleChoiceOptionRequest("Rio de Janeiro", false)
                )
        );

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithShortOption)))
                .andExpect(status().isBadRequest());

        String longText = "A".repeat(81);
        var requestWithLongOption = new MultipleChoiceTaskRequest(
                1L,
                "Capital do Brasil",
                1,
                List.of(
                        new MultipleChoiceOptionRequest(longText, true),
                        new MultipleChoiceOptionRequest("Rio de Janeiro", false)
                )
        );

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithLongOption)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_bad_request_when_option_equals_statement() throws Exception {
        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.BUILDING);

        var request = new MultipleChoiceTaskRequest(
                1L,
                "Capital do Brasil",
                1,
                List.of(new MultipleChoiceOptionRequest("Capital do Brasil", false),
                        new MultipleChoiceOptionRequest("Rio de Janeiro", false),
                        new MultipleChoiceOptionRequest("Fortaleza", false))
        );

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void should_return_bad_request_when_correct_or_incorrect_option_rules_are_not_followed() throws Exception {
        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.BUILDING);
        when(taskRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var requestWithOneCorrectOption = new MultipleChoiceTaskRequest(
                1L,
                "Capitais do Brasil",
                1,
                List.of(
                        new MultipleChoiceOptionRequest("Brasília", true),
                        new MultipleChoiceOptionRequest("São Paulo", false),
                        new MultipleChoiceOptionRequest("Rio de Janeiro", false)
                )
        );

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithOneCorrectOption)))
                .andExpect(status().isUnprocessableEntity());

        var requestWithAllCorrectOptions = new MultipleChoiceTaskRequest(
                1L,
                "Capitais do Brasil",
                1,
                List.of(
                        new MultipleChoiceOptionRequest("Brasília", true),
                        new MultipleChoiceOptionRequest("Rio de Janeiro", true),
                        new MultipleChoiceOptionRequest("São Paulo", true)
                )
        );

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithAllCorrectOptions)))
                .andExpect(status().isUnprocessableEntity());

        var validRequest = new MultipleChoiceTaskRequest(
                1L,
                "Capitais do Brasil",
                1,
                List.of(
                        new MultipleChoiceOptionRequest("Brasília", true),
                        new MultipleChoiceOptionRequest("Rio de Janeiro", true),
                        new MultipleChoiceOptionRequest("São Paulo", false)
                )
        );

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_bad_request_when_duplicate_options() throws Exception {
        Course course = mock(Course.class);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStatus()).thenReturn(Status.BUILDING);

        var request = new MultipleChoiceTaskRequest(
                1L,
                "Capital do Brasil",
                1,
        List.of(new MultipleChoiceOptionRequest("Brasília", false),
                new MultipleChoiceOptionRequest("Fortaleza", false),
                new MultipleChoiceOptionRequest("Brasília", true)
                )
        );

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnprocessableEntity());
    }
}

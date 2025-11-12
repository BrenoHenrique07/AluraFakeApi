package br.com.alura.AluraFake.user;

import br.com.alura.AluraFake.core.exception.handler.GlobalExceptionHandler;
import br.com.alura.AluraFake.domain.course.repository.CourseRepository;
import br.com.alura.AluraFake.domain.task.repository.TaskRepository;
import br.com.alura.AluraFake.domain.task.service.TaskService;
import br.com.alura.AluraFake.domain.user.controller.InstructorController;
import br.com.alura.AluraFake.domain.user.model.Role;
import br.com.alura.AluraFake.domain.user.model.User;
import br.com.alura.AluraFake.domain.user.repository.UserRepository;
import br.com.alura.AluraFake.domain.user.service.InstructorService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InstructorController.class)
@Import({InstructorService.class, GlobalExceptionHandler.class})
class InstructorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getInstructorCourses__should_return_404_when_instructor_not_found() throws Exception {
        Long instructorId = 99L;
        when(userRepository.findById(instructorId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/instructor/{id}/courses", instructorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Instructor not found with id " + instructorId));
    }

    @Test
    void getInstructorCourses__should_return_400_when_user_is_not_instructor() throws Exception {
        Long instructorId = 10L;
        User student = new User("João", "joao@test.com", Role.STUDENT);
        when(userRepository.findById(instructorId)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/instructor/{id}/courses", instructorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The user is not an instructor."));
    }

    @Test
    void getInstructorCourses__should_return_empty_list_when_instructor_has_no_courses() throws Exception {
        Long instructorId = 1L;
        User instructor = new User("Caio", "caio@alura.com", Role.INSTRUCTOR);

        when(userRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(courseRepository.findByInstructorId(instructorId)).thenReturn(List.of());

        mockMvc.perform(get("/instructor/{id}/courses", instructorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses").isEmpty())
                .andExpect(jsonPath("$.totalPublishedCourses").value(0));
    }

}

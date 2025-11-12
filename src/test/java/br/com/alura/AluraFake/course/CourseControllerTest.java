package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.core.exception.handler.GlobalExceptionHandler;
import br.com.alura.AluraFake.core.exception.type.BusinessRulesException;
import br.com.alura.AluraFake.core.exception.type.ResourceNotFoundException;
import br.com.alura.AluraFake.domain.course.controller.CourseController;
import br.com.alura.AluraFake.domain.course.dto.NewCourseDTO;
import br.com.alura.AluraFake.domain.course.model.Course;
import br.com.alura.AluraFake.domain.course.model.Status;
import br.com.alura.AluraFake.domain.course.repository.CourseRepository;
import br.com.alura.AluraFake.domain.course.service.CourseService;
import br.com.alura.AluraFake.domain.task.model.OpenTextTask;
import br.com.alura.AluraFake.domain.task.model.SingleChoiceTask;
import br.com.alura.AluraFake.domain.task.model.Task;
import br.com.alura.AluraFake.domain.task.repository.TaskRepository;
import br.com.alura.AluraFake.domain.task.service.TaskService;
import br.com.alura.AluraFake.domain.user.model.Role;
import br.com.alura.AluraFake.domain.user.model.User;
import br.com.alura.AluraFake.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@Import({CourseService.class, GlobalExceptionHandler.class})
class CourseControllerTest {

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
    void newCourseDTO__should_return_bad_request_when_email_is_invalid() throws Exception {

        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("paulo@alura.com.br");

        doReturn(Optional.empty()).when(userRepository)
                .findByEmail(newCourseDTO.getEmailInstructor());

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("emailInstructor"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }


    @Test
    void newCourseDTO__should_return_bad_request_when_email_is_no_instructor() throws Exception {

        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("paulo@alura.com.br");

        User user = mock(User.class);
        doReturn(false).when(user).isInstructor();

        doReturn(Optional.of(user)).when(userRepository)
                .findByEmail(newCourseDTO.getEmailInstructor());

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("emailInstructor"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void newCourseDTO__should_return_created_when_new_course_request_is_valid() throws Exception {

        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("paulo@alura.com.br");

        User user = mock(User.class);
        doReturn(true).when(user).isInstructor();

        doReturn(Optional.of(user)).when(userRepository).findByEmail(newCourseDTO.getEmailInstructor());

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isCreated());

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void listAllCourses__should_list_all_courses() throws Exception {
        User paulo = new User("Paulo", "paulo@alua.com.br", Role.INSTRUCTOR);

        Course java = new Course("Java", "Curso de java", paulo);
        Course hibernate = new Course("Hibernate", "Curso de hibernate", paulo);
        Course spring = new Course("Spring", "Curso de spring", paulo);

        when(courseRepository.findAll()).thenReturn(Arrays.asList(java, hibernate, spring));

        mockMvc.perform(get("/course/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java"))
                .andExpect(jsonPath("$[0].description").value("Curso de java"))
                .andExpect(jsonPath("$[1].title").value("Hibernate"))
                .andExpect(jsonPath("$[1].description").value("Curso de hibernate"))
                .andExpect(jsonPath("$[2].title").value("Spring"))
                .andExpect(jsonPath("$[2].description").value("Curso de spring"));
    }

    @Test
    void should_return_not_found_when_course_does_not_exist() throws Exception {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/course/{id}/publish", 99L))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(ResourceNotFoundException.class));
    }

    @Test
    void should_return_bad_request_when_course_status_is_not_building() throws Exception {
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1L);
        when(course.getStatus()).thenReturn(Status.PUBLISHED);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(post("/course/{id}/publish", 1L))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(BusinessRulesException.class)
                                .hasMessageContaining("Course must be in BUILDING status"));
    }

    @Test
    void should_return_bad_request_when_course_missing_task_types() throws Exception {
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1L);
        when(course.getStatus()).thenReturn(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        List<Task> tasks = List.of(new OpenTextTask("OpenTextTask", 1, course));
        when(taskRepository.findByCourseIdAndOrderGreaterThanEqualOrderByOrderAsc(1L, 1))
                .thenReturn(tasks);

        mockMvc.perform(post("/course/{id}/publish", 1L))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(BusinessRulesException.class)
                                .hasMessageContaining("must contain at least one activity of each type"));
    }

    @Test
    void should_return_bad_request_when_order_is_not_continuous() throws Exception {
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1L);
        when(course.getStatus()).thenReturn(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        List<Task> tasks = List.of(
                new OpenTextTask("OpenTextTask", 1, course),
                new SingleChoiceTask("SingleChoice", 3, course)
        );
        when(taskRepository.findByCourseIdAndOrderGreaterThanEqualOrderByOrderAsc(1L, 1))
                .thenReturn(tasks);

        mockMvc.perform(post("/course/{id}/publish", 1L))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(BusinessRulesException.class)
                                .hasMessageContaining("continuous sequential order"));
    }

}
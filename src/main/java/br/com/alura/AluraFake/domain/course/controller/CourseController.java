package br.com.alura.AluraFake.domain.course.controller;

import br.com.alura.AluraFake.domain.course.dto.PublishedCourseResponse;
import br.com.alura.AluraFake.domain.course.model.Course;
import br.com.alura.AluraFake.domain.course.dto.CourseListItemDTO;
import br.com.alura.AluraFake.domain.course.repository.CourseRepository;
import br.com.alura.AluraFake.domain.course.dto.NewCourseDTO;
import br.com.alura.AluraFake.domain.course.service.CourseService;
import br.com.alura.AluraFake.domain.user.model.User;
import br.com.alura.AluraFake.domain.user.repository.UserRepository;
import br.com.alura.AluraFake.core.exception.dto.ErrorItemDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseController(CourseService courseService, CourseRepository courseRepository, UserRepository userRepository){
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {

        //Caso implemente o bonus, pegue o instrutor logado
        Optional<User> possibleAuthor = userRepository
                .findByEmail(newCourse.getEmailInstructor())
                .filter(User::isInstructor);

        if(possibleAuthor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("emailInstructor", "Usuário não é um instrutor"));
        }

        Course course = new Course(newCourse.getTitle(), newCourse.getDescription(), possibleAuthor.get());

        courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/course/all")
    public ResponseEntity<List<CourseListItemDTO>> createCourse() {
        List<CourseListItemDTO> courses = courseRepository.findAll().stream()
                .map(CourseListItemDTO::new)
                .toList();
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/course/{id}/publish")
    public ResponseEntity<PublishedCourseResponse> createCourse(@PathVariable("id") Long id) {
        PublishedCourseResponse publishedCourseResponse = this.courseService.publish(id);
        return ResponseEntity.ok().body(publishedCourseResponse);
    }

}

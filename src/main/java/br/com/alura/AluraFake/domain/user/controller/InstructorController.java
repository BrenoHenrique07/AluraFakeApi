package br.com.alura.AluraFake.domain.user.controller;

import br.com.alura.AluraFake.domain.user.dto.InstructorCoursesResponse;
import br.com.alura.AluraFake.domain.user.service.InstructorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstructorController {

    private InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping("/instructor/{id}/courses")
    public ResponseEntity<InstructorCoursesResponse> getInstructorCourses(@PathVariable("id") Long id) {
        InstructorCoursesResponse instructorCoursesResponse = this.instructorService.getInstructorCourses(id);
        return ResponseEntity.ok().body(instructorCoursesResponse);
    }

}

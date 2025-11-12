package br.com.alura.AluraFake.domain.user.dto;

import br.com.alura.AluraFake.domain.course.dto.CourseSummaryResponse;

import java.util.List;

public record InstructorCoursesResponse(
        List<CourseSummaryResponse> courses,
        long totalPublishedCourses
) {
}

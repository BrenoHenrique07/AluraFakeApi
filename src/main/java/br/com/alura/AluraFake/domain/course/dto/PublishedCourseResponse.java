package br.com.alura.AluraFake.domain.course.dto;

import br.com.alura.AluraFake.domain.course.model.Course;
import br.com.alura.AluraFake.domain.course.model.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record PublishedCourseResponse(
        Long id,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        String title,
        String description,
        Long instructorId,
        Status status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime publishedAt
) {
    public static PublishedCourseResponse from(Course course) {
        return new PublishedCourseResponse(
                course.getId(),
                course.getCreatedAt(),
                course.getTitle(),
                course.getDescription(),
                course.getInstructor().getId(),
                course.getStatus(),
                course.getPublishedAt()
        );
    }
}

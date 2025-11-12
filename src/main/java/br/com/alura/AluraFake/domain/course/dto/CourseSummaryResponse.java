package br.com.alura.AluraFake.domain.course.dto;

import br.com.alura.AluraFake.domain.course.model.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CourseSummaryResponse(
        Long id,
        String title,
        Status status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime publishedAt,
        long totalTasks
) {
}

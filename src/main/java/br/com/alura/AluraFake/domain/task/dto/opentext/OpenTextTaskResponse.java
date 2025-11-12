package br.com.alura.AluraFake.domain.task.dto.opentext;

import br.com.alura.AluraFake.domain.task.model.OpenTextTask;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record OpenTextTaskResponse(
        Long id,
        String statement,
        int order,
        Long courseId,
        String courseTitle,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
) {
    public static OpenTextTaskResponse from(OpenTextTask task) {
        return new OpenTextTaskResponse(
                task.getId(),
                task.getStatement(),
                task.getOrder(),
                task.getCourse().getId(),
                task.getCourse().getTitle(),
                task.getCreatedAt()
        );
    }
}

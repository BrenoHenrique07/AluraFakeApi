package br.com.alura.AluraFake.domain.task.dto.opentext;

import br.com.alura.AluraFake.domain.task.model.OpenTextTask;

import java.time.LocalDateTime;

public record OpenTextTaskResponse(
        Long id,
        String statement,
        int order,
        Long courseId,
        String courseTitle,
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

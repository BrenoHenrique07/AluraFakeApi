package br.com.alura.AluraFake.domain.task.dto.singlechoice;

import br.com.alura.AluraFake.domain.task.model.SingleChoiceOption;
import br.com.alura.AluraFake.domain.task.model.SingleChoiceTask;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

public record SingleChoiceTaskResponse(
        Long id,
        String statement,
        int order,
        Long courseId,
        String courseTitle,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        List<SingleChoiceOptionResponse> options
) {
    public static SingleChoiceTaskResponse from(SingleChoiceTask task, List<SingleChoiceOption> options) {
        List<SingleChoiceOptionResponse> optionsResponse = options
                .stream()
                .map(SingleChoiceOptionResponse::from)
                .toList();

        return new SingleChoiceTaskResponse(
                task.getId(),
                task.getStatement(),
                task.getOrder(),
                task.getCourse().getId(),
                task.getCourse().getTitle(),
                task.getCreatedAt(),
                optionsResponse
        );
    }
}

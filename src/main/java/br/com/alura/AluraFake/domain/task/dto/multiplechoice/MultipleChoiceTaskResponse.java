package br.com.alura.AluraFake.domain.task.dto.multiplechoice;

import br.com.alura.AluraFake.domain.task.model.MultipleChoiceOption;
import br.com.alura.AluraFake.domain.task.model.MultipleChoiceTask;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record MultipleChoiceTaskResponse(
        Long id,
        String statement,
        int order,
        Long courseId,
        String courseTitle,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        List<MultipleChoiceOptionResponse> options
) {
    public static MultipleChoiceTaskResponse from(MultipleChoiceTask task, List<MultipleChoiceOption> options) {
        List<MultipleChoiceOptionResponse> optionsResponse = options
                .stream()
                .map(MultipleChoiceOptionResponse::from)
                .toList();

        return new MultipleChoiceTaskResponse(
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

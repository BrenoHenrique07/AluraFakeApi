package br.com.alura.AluraFake.domain.task.dto.singlechoice;

import br.com.alura.AluraFake.domain.task.model.SingleChoiceOption;
import br.com.alura.AluraFake.domain.task.model.SingleChoiceTask;

import java.time.LocalDateTime;
import java.util.List;

public record SingleChoiceTaskResponse(
        Long id,
        String statement,
        int order,
        Long courseId,
        String courseTitle,
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

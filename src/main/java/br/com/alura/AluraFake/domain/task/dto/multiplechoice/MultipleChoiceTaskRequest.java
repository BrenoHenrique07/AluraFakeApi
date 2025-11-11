package br.com.alura.AluraFake.domain.task.dto.multiplechoice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record MultipleChoiceTaskRequest(
        @NotNull(message = "courseId is required")
        Long courseId,

        @NotBlank(message = "statement cannot be blank")
        @Size(min = 4, max = 255, message = "statement must be between 4 and 255 characters")
        String statement,

        @NotNull(message = "order is required")
        @Min(value = 1, message = "order must be a positive integer")
        Integer order,

        @Valid
        @Size(min = 3, max = 5, message = "options must be between 3 and 5 items")
        List<MultipleChoiceOptionRequest> options
) {
    public MultipleChoiceTaskRequest {
        if (statement != null) {
            statement = statement.strip();
        }
    }
}

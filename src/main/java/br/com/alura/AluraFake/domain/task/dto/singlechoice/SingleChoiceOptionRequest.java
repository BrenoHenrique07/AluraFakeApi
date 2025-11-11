package br.com.alura.AluraFake.domain.task.dto.singlechoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SingleChoiceOptionRequest(
        @NotBlank(message = "option cannot be blank")
        @Size(min = 4, max = 80, message = "option must be between 4 and 80 characters")
        String option,

        @NotNull(message = "isCorrect is required")
        Boolean isCorrect
) {
    public SingleChoiceOptionRequest {
        if (option != null) {
            option = option.strip();
        }
    }
}

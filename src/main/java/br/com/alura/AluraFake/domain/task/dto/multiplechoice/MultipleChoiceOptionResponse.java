package br.com.alura.AluraFake.domain.task.dto.multiplechoice;

import br.com.alura.AluraFake.domain.task.model.MultipleChoiceOption;

public record MultipleChoiceOptionResponse(
        Long id,
        String option,
        boolean isCorrect
) {
    public static MultipleChoiceOptionResponse from(MultipleChoiceOption option) {
        return new MultipleChoiceOptionResponse(
                option.getId(),
                option.getOption(),
                option.isCorrect()
        );
    }
}

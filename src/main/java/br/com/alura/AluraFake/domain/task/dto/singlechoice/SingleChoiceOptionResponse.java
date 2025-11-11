package br.com.alura.AluraFake.domain.task.dto.singlechoice;

import br.com.alura.AluraFake.domain.task.model.SingleChoiceOption;

public record SingleChoiceOptionResponse(
        Long id,
        String option,
        boolean isCorrect
) {
    public static SingleChoiceOptionResponse from(SingleChoiceOption option) {
        return new SingleChoiceOptionResponse(
                option.getId(),
                option.getOption(),
                option.isCorrect()
        );
    }
}

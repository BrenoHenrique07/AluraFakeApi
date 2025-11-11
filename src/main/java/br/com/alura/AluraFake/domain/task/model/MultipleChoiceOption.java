package br.com.alura.AluraFake.domain.task.model;

import jakarta.persistence.*;

@Entity
@Table(name = "MultipleChoiceOption")
public class MultipleChoiceOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_text", nullable = false, length = 80)
    private String option;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private MultipleChoiceTask multipleChoiceTask;

    public MultipleChoiceOption() {
    }

    public MultipleChoiceOption(String option, boolean isCorrect, MultipleChoiceTask multipleChoiceTask) {
        this.option = option;
        this.isCorrect = isCorrect;
        this.multipleChoiceTask = multipleChoiceTask;
    }

    public Long getId() {
        return id;
    }

    public String getOption() {
        return option;
    }
    public void setOption(String option) {
        this.option = option;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public MultipleChoiceTask getMultipleChoiceTask() {
        return multipleChoiceTask;
    }
    public void setMultipleChoiceTask(MultipleChoiceTask multipleChoiceTask) {
        this.multipleChoiceTask = multipleChoiceTask;
    }
}

package br.com.alura.AluraFake.domain.task.model;

import jakarta.persistence.*;

@Entity
@Table(name = "SingleChoiceOption")
public class SingleChoiceOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_text", nullable = false, length = 80)
    private String option;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private SingleChoiceTask singleChoiceTask;

    public SingleChoiceOption() {
    }

    public SingleChoiceOption(String option, boolean isCorrect, SingleChoiceTask singleChoiceTask) {
        this.option = option;
        this.isCorrect = isCorrect;
        this.singleChoiceTask = singleChoiceTask;
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

    public SingleChoiceTask getSingleChoiceTask() {
        return singleChoiceTask;
    }
    public void setSingleChoiceTask(SingleChoiceTask singleChoiceTask) {
        this.singleChoiceTask = singleChoiceTask;
    }
}

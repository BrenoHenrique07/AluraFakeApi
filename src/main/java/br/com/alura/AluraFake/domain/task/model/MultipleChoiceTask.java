package br.com.alura.AluraFake.domain.task.model;

import br.com.alura.AluraFake.domain.course.model.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "MultipleChoiceTask")
public class MultipleChoiceTask extends Task {

    public MultipleChoiceTask() {
    }

    public MultipleChoiceTask(String statement, Integer order, Course course){
        super(statement, order, course);
    }

}

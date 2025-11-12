package br.com.alura.AluraFake.domain.task.model;

import br.com.alura.AluraFake.domain.course.model.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "SingleChoiceTask")
public class SingleChoiceTask extends Task {

    public SingleChoiceTask() {
    }

    public SingleChoiceTask(String statement, Integer order, Course course){
        super(statement, order, course, Type.SINGLE_CHOICE);
    }

}

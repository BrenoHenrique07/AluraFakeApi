package br.com.alura.AluraFake.domain.task.model;

import br.com.alura.AluraFake.domain.course.model.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "OpenTextTask")
public class OpenTextTask extends Task {

    public OpenTextTask() {
    }

    public OpenTextTask(String statement, Integer order, Course course){
        super(statement, order, course, Type.OPEN_TEXT);
    }

}
